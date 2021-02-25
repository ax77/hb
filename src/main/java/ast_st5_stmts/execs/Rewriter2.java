package ast_st5_stmts.execs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_st3_tac.LinearExpression;
import ast_st3_tac.ir.CopierNamer;
import ast_st3_tac.ir.FlatCodeItem;
import ast_st3_tac.items.FlatCallVoid;
import ast_st3_tac.leaves.Var;
import ast_stmt.StatementBase;
import ast_symtab.Scope;
import ast_symtab.ScopeLevels;
import ast_symtab.Symtab;
import ast_vars.VarBase;
import errors.AstParseException;
import hashed.Hash_ident;
import utils_oth.NullChecker;

public class Rewriter2 {

  private final Symtab<String, Var> variablesBlock;
  private final Symtab<String, Var> variablesLoop;
  private final List<LinearLoop> loops;

  public Rewriter2(LinearBlock input) {
    this.variablesBlock = new Symtab<>();
    this.variablesLoop = new Symtab<>();
    this.loops = new ArrayList<>();
    visitBlock(input);
  }

  private void visitBlock(LinearBlock currentBlockPtr) {
    NullChecker.check(currentBlockPtr);
    openBlockScope();
    for (final LinearStatement item : currentBlockPtr.getItems()) {
      visitStmt(item);
    }
    currentBlockPtr.setDestructors(genDestructors(variablesBlock));
    closeBlockScope();
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
  private void defineVars(LinearExpression linearExpression) {
    for (FlatCodeItem fc : linearExpression.getItems()) {

      //@formatter:off
      boolean isOk = 
         fc.isAssignVarAllocObject()  
      || fc.isAssignVarArrayAccess()  
      || fc.isAssignVarBinop()  
      || fc.isAssignVarFalse()  
      || fc.isAssignVarFieldAccess()  
      || fc.isAssignVarFlatCallClassCreationTmp()
      || fc.isAssignVarFlatCallResult()
      || fc.isAssignVarNull()   
      || fc.isAssignVarNum()   
      || fc.isAssignVarString()  
      || fc.isAssignVarTernaryOp()  
      || fc.isAssignVarTrue()   
      || fc.isAssignVarUnop()   
      || fc.isAssignVarVar();   
      //@formatter:on
      if (!isOk) {
        continue;
      }

      Var v = fc.getDest();
      variablesBlock.addsym(v.getName().getName(), v);

      if (!loops.isEmpty()) {
        variablesLoop.addsym(v.getName().getName(), v);
      }
    }
  }

  public void openBlockScope() {
    this.variablesBlock.pushscope(ScopeLevels.BLOCK_SCOPE, "B");
  }

  public void closeBlockScope() {
    this.variablesBlock.popscope();
  }

  public void openLoopScope() {
    this.variablesLoop.pushscope(ScopeLevels.BLOCK_SCOPE, "L");
  }

  public void closeLoopScope() {
    this.variablesLoop.popscope();
  }

  private List<Var> getAllVarsDefinedAbove(Symtab<String, Var> scope) {
    List<Var> res = new ArrayList<>();
    Scope<String, Var> current = scope.peekScope();
    for (Entry<String, Var> ent : current.getScope().entrySet()) {
      final Var var = ent.getValue();
      if (!var.getType().is_class()) {
        continue;
      }
      if (var.is(VarBase.METHOD_PARAMETER)) {
        continue;
      }
      if (!res.contains(var)) {
        res.add(var);
      }
    }
    Collections.sort(res);
    return res;
  }

  private LocalDestructors genDestructors(Symtab<String, Var> scope) {
    List<Var> vars = getAllVarsDefinedAbove(scope);
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

  private void visitStmt(LinearStatement s) {
    NullChecker.check(s);
    final StatementBase base = s.getBase();
    if (base == StatementBase.SIF) {
      rewriteSelection(s);
    } else if (base == StatementBase.SEXPR) {
      rewriteSexpr(s);
    } else if (base == StatementBase.SBLOCK) {
      rewriteBlock(s);
    } else if (base == StatementBase.SRETURN) {
      rewriteReturn(s);
    } else if (base == StatementBase.SFOR) {
      rewriteLoop(s);
    } else if (base == StatementBase.SBREAK) {
      rewriteBreak(s);
    } else if (base == StatementBase.SCONTINUE) {
      rewriteContinue(s);
    } else if (base == StatementBase.SVAR_DECLARATION) {
      rewriteVarDecl(s);
    } else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }
  }

  private void rewriteVarDecl(LinearStatement s) {
    defineVars(s.getLinearExpression());
  }

  private void rewriteContinue(LinearStatement s) {
    final LinearContinue linearContinue = s.getLinearContinue();
    final LinearLoop loop = linearContinue.getLoop();
    if (loop.hasStep()) {
      defineVars(loop.getStep());
    }
    linearContinue.setDestructors(genDestructors(variablesLoop));
  }

  private void rewriteBreak(LinearStatement s) {
    LinearBreak brk = s.getLinearBreak();
    brk.setDestructors(genDestructors(variablesLoop));
  }

  private void rewriteLoop(LinearStatement s) {
    openLoopScope();

    LinearLoop loop = s.getLinearLoop();
    pushLoop(loop);

    visitBlock(loop.getBlock());

    popLoop();
    closeLoopScope();
  }

  private void rewriteReturn(LinearStatement s) {
    LinearReturn linearReturn = s.getLinearReturn();
  }

  private void rewriteBlock(LinearStatement s) {
    final LinearBlock linearBlock = s.getLinearBlock();
    linearBlock.setDestructors(genDestructors(variablesBlock));

    visitBlock(linearBlock);
  }

  private void rewriteSexpr(LinearStatement s) {
    defineVars(s.getLinearExpression());
  }

  private void rewriteSelection(LinearStatement s) {
    final LinearSelection linearSelection = s.getLinearSelection();
    visitBlock(linearSelection.getTrueBlock());
    if (linearSelection.hasElse()) {
      visitBlock(linearSelection.getElseBlock());
    }
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