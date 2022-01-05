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
  private final ClassDeclaration object;
  private final ClassMethodDeclaration method;

  public ApplyStatement(SymbolTable symtabApplier, ClassDeclaration object, ClassMethodDeclaration method) {
    this.symtabApplier = symtabApplier;
    this.object = object;
    this.method = method;
  }

  public void applyStatement(final StmtStatement s) {

    if (s == null) {
      return;
    }

    StatementBase base = s.getBase();
    if (base == StatementBase.SIF) {
      visitSelectionStmt(s.getIfStmt());
    } else if (base == StatementBase.SEXPR) {
      visitExprStmt(s);
    } else if (base == StatementBase.SBLOCK) {
      visitBlock(s.getBlockStmt());
    } else if (base == StatementBase.SRETURN) {
      visitReturn(s.getReturnStmt());
    } else if (base == StatementBase.SFOR) {
      visitFor(s);
    } else if (base == StatementBase.SBREAK) {
      visitBreak(s);
    } else if (base == StatementBase.SCONTINUE) {
      visitContinue(s);
    } else if (base == StatementBase.SVAR_DECLARATION) {
      visitLocalVar(s.getLocalVariable());
    } else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }

  }

  private void visitBreak(StmtStatement s) {

  }

  private void visitContinue(StmtStatement s) {

  }

  private void visitExprStmt(final StmtStatement node) {
    applyExpression(node.getExprStmt());
    semExprStmt(node);
  }

  private void visitReturn(final StmtReturn node) {
    applyExpression(node.getExpression());
    semReturn(node);
  }

  private void visitFor(StmtStatement s) {

    StmtFor node = s.getForStmt();

    symtabApplier.openBlockScope();
    applyExpression(node.getTest());
    applyExpression(node.getStep());
    visitBlock(node.getBlock());
    symtabApplier.closeBlockScope();

    semFor(s);
  }

  private void visitSelectionStmt(final StmtSelect node) {
    applyExpression(node.getCondition());
    visitBlock(node.getTrueStatement());
    visitBlock(node.getOptionalElseStatement());
    semSelection(node);
  }

  private void visitBlock(final StmtBlock block) {

    /// if without else, etc...
    if (block == null) {
      return;
    }

    symtabApplier.openBlockScope();

    for (StmtStatement item : block.getBlockItems()) {
      applyStatement(item);
    }

    symtabApplier.closeBlockScope();
  }

  private void visitLocalVar(final VarDeclarator var) {

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

    applyInitializer(var);
  }

  private void applyExpression(ExprExpression e) {
    ApplyExpression applier = new ApplyExpression(symtabApplier, object, method);
    applier.applyExpression(e);
  }

  private void applyInitializer(VarDeclarator var) {
    ApplyInitializer applier = new ApplyInitializer(symtabApplier, object, method);
    applier.applyInitializer(var);

  }

  /// 3ac-semantic

  private void semExprStmt(final StmtStatement node) {
  }

  private void semReturn(final StmtReturn node) {
  }

  private void semFor(StmtStatement s) {
    StmtFor node = s.getForStmt();
    checkIsBoolean(node.getTest());
  }

  private void semSelection(final StmtSelect node) {
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
