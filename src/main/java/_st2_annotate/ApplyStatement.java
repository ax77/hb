package _st2_annotate;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_method.ClassMethodDeclaration;
import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtFor;
import ast_stmt.StmtReturn;
import ast_stmt.StmtSelect;
import ast_stmt.StmtStatement;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import errors.ErrorLocation;

public class ApplyStatement {

  private final SymbolTable symtabApplier;

  public ApplyStatement(SymbolTable symtabApplier) {
    this.symtabApplier = symtabApplier;
  }

  public void applyStatement(final ClassDeclaration object, final ClassMethodDeclaration method,
      final StmtStatement s) {

    if (s == null) {
      return;
    }

    StatementBase base = s.getBase();
    if (base == StatementBase.SIF) {
      visitSelectionStmt(object, method, s.getIfStmt());
    } else if (base == StatementBase.SEXPR) {
      visitExprStmt(object, method, s);
    } else if (base == StatementBase.SBLOCK) {
      visitBlock(object, method, s.getBlockStmt());
    } else if (base == StatementBase.SRETURN) {
      visitReturn(object, method, s.getReturnStmt());
    } else if (base == StatementBase.SFOR) {
      visitFor(object, method, s);
    } else if (base == StatementBase.SBREAK) {
      visitBreak(object, method, s);
    } else if (base == StatementBase.SCONTINUE) {
      visitContinue(object, method, s);
    } else if (base == StatementBase.SVAR_DECLARATION) {
      visitLocalVar(object, method, s.getLocalVariable());
    } else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }

  }

  private void visitBreak(ClassDeclaration object, ClassMethodDeclaration method, StmtStatement s) {

  }

  private void visitContinue(ClassDeclaration object, ClassMethodDeclaration method, StmtStatement s) {

  }

  private void visitExprStmt(final ClassDeclaration object, ClassMethodDeclaration method, final StmtStatement node) {
    applyExpression(object, node.getExprStmt());
    semExprStmt(object, method, node);
  }

  private void visitReturn(final ClassDeclaration object, ClassMethodDeclaration method, final StmtReturn node) {
    applyExpression(object, node.getExpression());
    semReturn(object, method, node);
  }

  private void visitFor(ClassDeclaration object, ClassMethodDeclaration method, StmtStatement s) {

    StmtFor node = s.getForStmt();

    symtabApplier.openBlockScope(node.getBlock());
    applyExpression(object, node.getTest());
    applyExpression(object, node.getStep());
    visitBlock(object, method, node.getBlock());
    symtabApplier.closeBlockScope();

    semFor(object, method, s);
  }

  private void visitSelectionStmt(final ClassDeclaration object, final ClassMethodDeclaration method,
      final StmtSelect node) {
    applyExpression(object, node.getCondition());
    visitBlock(object, method, node.getTrueStatement());
    visitBlock(object, method, node.getOptionalElseStatement());
    semSelection(object, method, node);
  }

  private void visitBlock(final ClassDeclaration object, final ClassMethodDeclaration method, final StmtBlock block) {

    /// if without else, etc...
    if (block == null) {
      return;
    }

    symtabApplier.openBlockScope(block);

    for (StmtStatement item : block.getBlockItems()) {
      applyStatement(object, method, item);
    }

    symtabApplier.closeBlockScope();
  }

  private void visitLocalVar(final ClassDeclaration object, ClassMethodDeclaration method, final VarDeclarator var) {

    if (var.is(VarBase.METHOD_VAR)) {
      if (symtabApplier.howMuchBlocks() != 1) {
        throw new AstParseException("aux. error, too much blocks for method-variable declaration. ");
      }
      symtabApplier.defineMethodVariable(method, var);
    } else {
      if (symtabApplier.howMuchBlocks() == 1) {
        throw new AstParseException("aux. error, too few blocks for local-variable declaration. ");
      }
      symtabApplier.defineBlockVar(var);
    }

    applyInitializer(object, var);
  }

  private void applyExpression(ClassDeclaration object, ExprExpression e) {
    ApplyExpression applier = new ApplyExpression(symtabApplier);
    applier.applyExpression(object, e);
  }

  private void applyInitializer(ClassDeclaration object, VarDeclarator var) {
    ApplyInitializer applier = new ApplyInitializer(symtabApplier);
    applier.applyInitializer(object, var);

  }

  /// 3ac-semantic

  private void semExprStmt(ClassDeclaration object, ClassMethodDeclaration method, final StmtStatement node) {
  }

  private void semReturn(ClassDeclaration object, ClassMethodDeclaration method, final StmtReturn node) {
  }

  private void semFor(ClassDeclaration object, ClassMethodDeclaration method, StmtStatement s) {
    StmtFor node = s.getForStmt();
    checkIsBoolean(node.getTest());
  }

  private void semSelection(ClassDeclaration object, ClassMethodDeclaration method, final StmtSelect node) {
    checkIsBoolean(node.getCondition());
  }

  private void checkIsBoolean(ExprExpression e) {
    // that's ok - the test expression of the for-loop must ne null
    // for example.
    if (e == null) {
      return;
    }
    if (!e.getResultType().isBoolean()) {
      ErrorLocation.errorExpression("expected boolean type: ", e);
    }
  }

}
