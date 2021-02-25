package ast_st5_stmts.execs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_expr.ExprUnary;
import ast_method.ClassMethodDeclaration;
import ast_st3_tac.LinearExpression;
import ast_st3_tac.LinearExpressionBuilder;
import ast_st3_tac.ir.CopierNamer;
import ast_st3_tac.items.FlatCallVoid;
import ast_st3_tac.leaves.Var;
import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtFor;
import ast_stmt.StmtReturn;
import ast_stmt.StmtStatement;
import ast_symtab.Scope;
import ast_symtab.ScopeLevels;
import ast_symtab.Symtab;
import ast_types.TypeBindings;
import ast_vars.VarBase;
import errors.AstParseException;
import hashed.Hash_ident;
import tokenize.T;
import tokenize.Token;
import utils_oth.NullChecker;

public class Rewriter {

  private final ClassMethodDeclaration method;
  private final LinearBlock result;
  private final List<LinearLoop> loops;
  private final Symtab<String, Var> variablesLoop;

  public Rewriter(ClassMethodDeclaration method) {
    this.method = method;
    this.result = new LinearBlock();
    this.loops = new ArrayList<>();
    this.variablesLoop = new Symtab<>();

    visitBlock(method.getBlock(), result);
  }

  /// SYMTAB 
  /// we have to trace all the variables defined in a loop
  /// because we should generate destructors before each
  /// break or continue statement for each var defined 
  /// ABOVE, NOT for each variable in a loop...
  /// ::
  /// for(int i=0; i<8 i+=1) {
  ///   str s1 = new str();
  ///   
  ///   if(i==2) {
  ///     @ delete s1;
  ///     break;
  ///   }
  ///   
  ///   str s2 = new str();
  ///   
  ///   if(i == 3) {
  ///     @ delete s1;
  ///     @ delete s2;
  ///     continue;
  ///   }
  /// }
  private void defineVars(List<Var> allVars) {
    for (Var v : allVars) {
      variablesLoop.addsym(UUID.randomUUID().toString(), v);
    }
  }

  public void openLoopScope(String blockId) {
    this.variablesLoop.pushscope(ScopeLevels.BLOCK_SCOPE, blockId);
  }

  public void closeLoopScope() {
    this.variablesLoop.popscope();
  }

  private List<Var> getAllVarsDefinedAbove() {
    List<Var> res = new ArrayList<>();
    Scope<String, Var> current = variablesLoop.peekScope();
    for (Entry<String, Var> ent : current.getScope().entrySet()) {
      final Var var = ent.getValue();
      if (!var.getType().is_class()) {
        continue;
      }
      if (!var.is(VarBase.LOCAL_VAR)) {
        continue;
      }
      if (!res.contains(var)) {
        res.add(var);
      }
    }
    Collections.sort(res);
    return res;
  }

  private LocalDestructors genDestructors() {
    List<Var> vars = getAllVarsDefinedAbove();
    LocalDestructors res = new LocalDestructors();
    for (Var v : vars) {
      List<Var> args = new ArrayList<>();
      args.add(v);
      final ClassDeclaration classType = v.getType().getClassTypeFromRef();
      final ClassMethodDeclaration destructor = classType.getDestructor();
      FlatCallVoid fc = new FlatCallVoid(Hash_ident.getHashedIdent(CopierNamer.getMethodName(destructor)), args);
      res.add(fc);
    }
    return res;
  }

  /// 

  public LinearBlock getResult() {
    return result;
  }

  private void visitBlock(StmtBlock block, LinearBlock currentBlockPtr) {
    NullChecker.check(block, currentBlockPtr);
    for (final StmtStatement item : block.getBlockItems()) {
      visitStmt(item, currentBlockPtr);
    }
  }

  private void visitStmt(final StmtStatement s, final LinearBlock currentBlockPtr) {
    NullChecker.check(s, currentBlockPtr);
    final StatementBase base = s.getBase();
    if (base == StatementBase.SIF) {
      rewriteSelection(s, currentBlockPtr);
    } else if (base == StatementBase.SEXPR) {
      rewriteSexpr(s, currentBlockPtr);
    } else if (base == StatementBase.SBLOCK) {
      rewriteBlock(s, currentBlockPtr);
    } else if (base == StatementBase.SRETURN) {
      rewriteReturn(s, currentBlockPtr);
    } else if (base == StatementBase.SFOR) {
      rewriteLoop(s, currentBlockPtr);
    } else if (base == StatementBase.SBREAK) {
      rewriteBreak(currentBlockPtr);
    } else if (base == StatementBase.SCONTINUE) {
      rewriteContinue(currentBlockPtr);
    } else if (base == StatementBase.SVAR_DECLARATION) {
      rewriteVarDecl(s, currentBlockPtr);
    } else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }
  }

  private void rewriteSelection(final StmtStatement s, final LinearBlock currentBlockPtr) {
    /// 1) linearize the if-expression, and put it into the block
    LinearExpression lexpr = LinearExpressionBuilder.build(s.getIfStmt().getCondition(), method.getClazz(), method);
    currentBlockPtr.pushItemBack(new LinearStatement(StatementBase.SEXPR, lexpr));

    /// 2) create the if-statement with the result variable
    LinearSelection linearSelection = new LinearSelection(lexpr.getDest());
    currentBlockPtr.pushItemBack(new LinearStatement(linearSelection));

    /// 3) visit the true block of the created if, with the true-statement 
    /// of the original block.
    visitBlock(s.getIfStmt().getTrueStatement(), linearSelection.getTrueBlock());

    if (s.getIfStmt().hasElse()) {
      linearSelection.setElseBlock(new LinearBlock());
      visitBlock(s.getIfStmt().getOptionalElseStatement(), linearSelection.getElseBlock());
    }
  }

  private void rewriteSexpr(final StmtStatement s, final LinearBlock currentBlockPtr) {
    LinearExpression lexpr = LinearExpressionBuilder.build(s.getExprStmt(), method.getClazz(), method);
    currentBlockPtr.pushItemBack(new LinearStatement(StatementBase.SEXPR, lexpr));
  }

  private void rewriteBlock(final StmtStatement s, final LinearBlock currentBlockPtr) {
    final LinearStatement nestedStmt = new LinearStatement(new LinearBlock());
    currentBlockPtr.pushItemBack(nestedStmt);

    visitBlock(s.getBlockStmt(), nestedStmt.getLinearBlock());
  }

  private void rewriteReturn(final StmtStatement s, final LinearBlock currentBlockPtr) {
    StmtReturn stmtReturn = s.getReturnStmt();

    if (stmtReturn.hasExpression()) {

      /// 1) linearize the if-expression, and put it into the block
      LinearExpression lexpr = LinearExpressionBuilder.build(stmtReturn.getExpression(), method.getClazz(), method);
      currentBlockPtr.pushItemBack(new LinearStatement(StatementBase.SEXPR, lexpr));

      /// 2) create the if-statement with the result variable
      LinearReturn linearReturn = new LinearReturn(lexpr.getDest());
      currentBlockPtr.pushItemBack(new LinearStatement(linearReturn));
    }

    else {
      LinearReturn linearReturn = new LinearReturn(null);
      currentBlockPtr.pushItemBack(new LinearStatement(linearReturn));
    }
  }

  private void rewriteVarDecl(final StmtStatement s, final LinearBlock currentBlockPtr) {
    LinearExpression lvars = LinearExpressionBuilder.build(s.getLocalVariable(), method.getClazz(), method);
    currentBlockPtr.pushItemBack(new LinearStatement(StatementBase.SVAR_DECLARATION, lvars));
  }

  private void rewriteBreak(final LinearBlock currentBlockPtr) {
    /// TODO: this statement has destructors from 
    /// the top of the loop to this particularly
    /// point... all variables defined below are ignored.
    ///
    LinearBreak label = new LinearBreak(peekLoop());
    currentBlockPtr.pushItemBack(new LinearStatement(label));
  }

  private void rewriteContinue(final LinearBlock currentBlockPtr) {

    /// TODO: this statement has destructors from 
    /// the top of the loop to this particularly
    /// point... all variables defined below are ignored.
    ///
    /// TODO: can we use the previously expanded expression,
    /// or generate the new one from the original for-loop ExprExpression-step?
    ///
    final LinearLoop theLoopOfTheContinue = peekLoop();
    if (theLoopOfTheContinue.hasStep()) {

      /// we should append the 'step' expression
      /// if it was a for-loop before 'continue'
      currentBlockPtr.pushItemBack(new LinearStatement(StatementBase.SEXPR, theLoopOfTheContinue.getStep()));
    }

    LinearContinue label = new LinearContinue(theLoopOfTheContinue);
    currentBlockPtr.pushItemBack(new LinearStatement(label));
  }

  private void rewriteLoop(final StmtStatement s, final LinearBlock currentBlockPtr) {
    final StmtFor forStmt = s.getForStmt();
    final ExprExpression test = forStmt.getTest();
    final ExprExpression step = forStmt.getStep();

    final LinearLoop resultLoop = new LinearLoop();
    pushLoop(resultLoop);
    currentBlockPtr.pushItemBack(new LinearStatement(resultLoop));

    if (test != null) {
      final Token beginPos = test.getBeginPos();
      final ExprUnary un = new ExprUnary(new Token(beginPos, "!", T.T_EXCLAMATION), test);
      final ExprExpression eNot = new ExprExpression(un, beginPos);
      eNot.setResultType(TypeBindings.make_boolean(beginPos));

      /// 1) create the first item of the for-block, and append this expression as
      /// an item of a for-loop-block
      /// if (!test) { break; }
      LinearExpression testExpr = LinearExpressionBuilder.build(eNot, method.getClazz(), method);
      resultLoop.setTest(testExpr);
      resultLoop.getBlock().pushItemBack(new LinearStatement(StatementBase.SEXPR, testExpr));

      /// NOTE: this statement guard has no destructors, bacause it is a
      /// first statement in a loop, and it know nothing about any variable
      /// which will be defined later.

      LinearSelection breakGuard = new LinearSelection(testExpr.getDest());
      final LinearBreak linearBreak = new LinearBreak(peekLoop());
      breakGuard.getTrueBlock().pushItemBack(new LinearStatement(linearBreak));

      /// 2) append the test-guard
      resultLoop.getBlock().pushItemBack(new LinearStatement(breakGuard));
    }

    /// the 'step' is a last statement of the for-loop-block
    /// but we should expand it later, if we will see any break
    /// or continue;
    if (step != null) {
      LinearExpression stepExpr = LinearExpressionBuilder.build(step, method.getClazz(), method);
      resultLoop.setStep(stepExpr);
    }

    /// visit the block, and we know the test, and the step
    /// for break and continue statements.
    visitBlock(forStmt.getBlock(), resultLoop.getBlock());

    if (resultLoop.hasStep()) {
      resultLoop.getBlock().pushItemBack(new LinearStatement(StatementBase.SEXPR, resultLoop.getStep()));
    }

    popLoop();
  }

  /// LOOPS stack

  private void pushLoop(LinearLoop s) {
    loops.add(0, s);
  }

  private void popLoop() {
    loops.remove(0);
  }

  private LinearLoop peekLoop() {
    return loops.get(0);
  }

}
