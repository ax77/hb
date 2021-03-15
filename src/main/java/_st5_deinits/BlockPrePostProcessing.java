package _st5_deinits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import _st3_linearize_expr.LinearExpression;
import _st3_linearize_expr.ir.FlatCodeItem;
import _st3_linearize_expr.ir.VarCreator;
import _st3_linearize_expr.items.FlatCallVoid;
import _st3_linearize_expr.leaves.Var;
import _st4_linearize_stmt.LinearBlock;
import _st4_linearize_stmt.items.LinearBreak;
import _st4_linearize_stmt.items.LinearContinue;
import _st4_linearize_stmt.items.LinearLoop;
import _st4_linearize_stmt.items.LinearReturn;
import _st4_linearize_stmt.items.LinearSelection;
import _st4_linearize_stmt.items.LinearStatement;
import _st7_codeout.AuxNames;
import _st4_linearize_stmt.items.BlockPrePost;
import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_stmt.StatementBase;
import ast_symtab.Scope;
import ast_symtab.ScopeLevels;
import ast_symtab.Symtab;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import utils_oth.NullChecker;

public class BlockPrePostProcessing {

  private final Symtab<String, Var> variablesBlock;
  private final Symtab<String, Var> variablesLoop;
  private final List<LinearLoop> loops;
  private final ClassMethodDeclaration method;

  public BlockPrePostProcessing(ClassMethodDeclaration method) {
    this.variablesBlock = new Symtab<>();
    this.variablesLoop = new Symtab<>();
    this.loops = new ArrayList<>();
    this.method = method;
  }

  private BlockPrePost onEnter() {
    BlockPrePost rv = new BlockPrePost();

    rv.add(openCloseFrame('o'));

    List<FlatCallVoid> asserts = new ArrayList<>();
    List<FlatCallVoid> regPtrs = new ArrayList<>();

    for (VarDeclarator var : method.getParameters()) {
      if (!var.getType().isClass()) {
        continue;
      }
      Var arg = VarCreator.copyVarDecl(var);

      List<Var> args = new ArrayList<>();
      args.add(arg);
      asserts.add(new FlatCallVoid(AuxNames.ASSERT, args));
      regPtrs.add(new FlatCallVoid(AuxNames.REG_PTR_IN_A_FRAME, args));
    }

    for (FlatCallVoid fc : asserts) {
      rv.add(fc);
    }

    for (FlatCallVoid fc : regPtrs) {
      rv.add(fc);
    }

    return rv;
  }

  private FlatCallVoid openCloseFrame(char oc) {
    String s = AuxNames.OPEN_FRAME;
    if (oc == 'c') {
      s = AuxNames.CLOSE_FRAME;
    }
    return new FlatCallVoid(s, new ArrayList<>());
  }

  public void apply(LinearBlock input) {
    input.setOnEnter(onEnter());
    visitBlock(input);

    if (!input.theLastItemIsReturn()) {
      input.getOnExit().add(openCloseFrame('c'));
    }
  }

  private void visitBlock(LinearBlock currentBlockPtr) {
    NullChecker.check(currentBlockPtr);
    openBlockScope();
    for (final LinearStatement item : currentBlockPtr.getItems()) {
      visitStmt(item);
    }
    if (!currentBlockPtr.theLastItemIsReturn()) {
      if (!method.isDestructor()) {
        currentBlockPtr.setOnExit(genDestructorsForGivenScope(variablesBlock));
      }
    }
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

      if (!fc.isOneOfAssigns()) {
        continue;
      }

      Var v = fc.getDest();
      variablesBlock.addsym(v.getName().getName(), v);

      if (!loops.isEmpty()) {
        variablesLoop.addsym(v.getName().getName(), v);
      }
    }
  }

  private void openBlockScope() {
    this.variablesBlock.pushscope(ScopeLevels.BLOCK_SCOPE);
  }

  private void closeBlockScope() {
    this.variablesBlock.popscope();
  }

  private void openLoopScope() {
    this.variablesLoop.pushscope(ScopeLevels.BLOCK_SCOPE);
  }

  private void closeLoopScope() {
    this.variablesLoop.popscope();
  }

  private List<Var> buildVarListFromGivenScope(Scope<String, Var> current) {
    List<Var> res = new ArrayList<>();
    for (Entry<String, Var> ent : current.getScope().entrySet()) {
      final Var var = ent.getValue();
      if (!var.getType().isClass()) {
        continue;
      }
      if (var.is(VarBase.METHOD_PARAMETER)) {
        continue;
      }
      if (!res.contains(var)) {
        res.add(var);
      }
    }
    return res;
  }

  private List<Var> getAllVarsDefinedAbove(Symtab<String, Var> scope) {
    Scope<String, Var> current = scope.peekScope();
    List<Var> res = buildVarListFromGivenScope(current);

    Collections.sort(res);
    return res;
  }

  private List<Var> getAllVarsDefinedAboveToTop(Symtab<String, Var> scope) {
    List<Var> res = new ArrayList<>();
    List<Scope<String, Var>> scopes = scope.getScopes();
    for (Scope<String, Var> s : scopes) {
      res.addAll(buildVarListFromGivenScope(s));
    }
    Collections.sort(res);
    return res;
  }

  private BlockPrePost genDestructorsFromCurrentScopeToTop(Symtab<String, Var> scope) {
    List<Var> vars = getAllVarsDefinedAboveToTop(scope);
    return genDestructorsForGivenVars(vars);
  }

  private BlockPrePost genDestructorsForGivenScope(Symtab<String, Var> scope) {
    List<Var> vars = getAllVarsDefinedAbove(scope);
    return genDestructorsForGivenVars(vars);
  }

  private BlockPrePost genDestructorsForGivenVars(List<Var> vars) {
    BlockPrePost res = new BlockPrePost();
    for (Var v : vars) {
      List<Var> args = new ArrayList<>();
      args.add(v);

      if (v.getType().isClass()) {
        if (!v.isOriginalNoTempVar()) {
          continue;
        }

        final ClassDeclaration classType = v.getType().getClassTypeFromRef();
        final ClassMethodDeclaration destructor = classType.getDestructor();

        FlatCallVoid fc = new FlatCallVoid(destructor.signToStringCall(), args);
        res.add(fc);
      }
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
    linearContinue.setDestructors(genDestructorsForGivenScope(variablesLoop));
  }

  private void rewriteBreak(LinearStatement s) {
    LinearBreak brk = s.getLinearBreak();
    brk.setDestructors(genDestructorsForGivenScope(variablesLoop));
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

    /// TODO: rewrite this more clean
    final BlockPrePost destructors = genDestructorsFromCurrentScopeToTop(variablesBlock);
    final BlockPrePost withoutTheVar = new BlockPrePost();

    if (linearReturn.hasResult()) {
      for (FlatCallVoid fc : destructors.getDestructors()) {
        Var v = fc.getArgs().get(0);
        if (v.equals(linearReturn.getResult())) {
          continue;
        }
        withoutTheVar.add(fc);
      }

      withoutTheVar.add(openCloseFrame('c'));
      linearReturn.setDestructors(withoutTheVar);
    }

    else {
      destructors.add(openCloseFrame('c'));
      linearReturn.setDestructors(destructors);
    }

  }

  private void rewriteBlock(LinearStatement s) {
    final LinearBlock linearBlock = s.getLinearBlock();
    linearBlock.setOnExit(genDestructorsForGivenScope(variablesBlock));

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
