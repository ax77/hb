package ast_st5_stmts.execs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_st3_tac.LinearExpression;
import ast_st3_tac.ir.CopierNamer;
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

  public Rewriter2(LinearBlock input) {
    this.variablesBlock = new Symtab<>();
    visitBlock(input);
  }

  private void visitBlock(LinearBlock currentBlockPtr) {
    NullChecker.check(currentBlockPtr);
    openScope();
    for (final LinearStatement item : currentBlockPtr.getItems()) {
      visitStmt(item);
    }
    currentBlockPtr.setDestructors(genDestructors());
    closeScope();
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
    for (Var v : linearExpression.getAllVars()) {
      variablesBlock.addsym(v.getName().getName(), v);
    }
  }

  public void openScope() {
    this.variablesBlock.pushscope(ScopeLevels.BLOCK_SCOPE, "_");
  }

  public void closeScope() {
    this.variablesBlock.popscope();
  }

  private List<Var> getAllVarsDefinedAbove() {
    List<Var> res = new ArrayList<>();
    Scope<String, Var> current = variablesBlock.peekScope();
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
      //openScope();
      rewriteLoop(s);
      //closeScope();
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
    linearContinue.setDestructors(genDestructors());
  }

  private void rewriteBreak(LinearStatement s) {
    LinearBreak brk = s.getLinearBreak();
    brk.setDestructors(genDestructors());
  }

  private void rewriteLoop(LinearStatement s) {
    LinearLoop loop = s.getLinearLoop();
    visitBlock(loop.getBlock());
  }

  private void rewriteReturn(LinearStatement s) {
    LinearReturn linearReturn = s.getLinearReturn();
  }

  private void rewriteBlock(LinearStatement s) {

    final LinearBlock linearBlock = s.getLinearBlock();
    linearBlock.setDestructors(genDestructors());

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

}
