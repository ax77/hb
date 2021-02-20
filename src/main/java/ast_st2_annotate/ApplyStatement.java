package ast_st2_annotate;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_method.ClassMethodDeclaration;
import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
import ast_stmt.StmtFor;
import ast_stmt.StmtReturn;
import ast_stmt.StmtSelect;
import ast_stmt.StmtStatement;
import ast_stmt.StmtWhile;
import ast_vars.VarDeclarator;
import errors.AstParseException;

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
    if (base == StatementBase.SFOREACH_TMP) {
      visitForeach(object, method, s);
    } else if (base == StatementBase.SIF) {
      visitSelectionStmt(object, method, s.getIfStmt());
    } else if (base == StatementBase.SEXPR) {
      applyExpression(object, s.getExprStmt());
    } else if (base == StatementBase.SBLOCK) {
      visitBlock(object, method, s.getBlockStmt());
    } else if (base == StatementBase.SRETURN) {
      visitReturn(object, s.getReturnStmt());
    } else if (base == StatementBase.SWHILE) {
      visitWhile(object, method, s.getWhileStmt());
    } else if (base == StatementBase.SFOR) {
      visitVor(object, method, s.getForStmt());
    } else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }

  }

  private void visitReturn(final ClassDeclaration object, final StmtReturn returnStmt) {
    if (returnStmt.hasExpression()) {
      applyExpression(object, returnStmt.getExpression());
    }
  }

  private void visitVor(ClassDeclaration object, ClassMethodDeclaration method, StmtFor stmtFor) {

    symtabApplier.openBlockScope("block");
    visitLocalVar(object, stmtFor.getDecl());

    applyExpression(object, stmtFor.getInit());
    applyExpression(object, stmtFor.getTest());
    applyExpression(object, stmtFor.getStep());
    visitBlock(object, method, stmtFor.getBlock());

    symtabApplier.closeBlockScope();
  }

  private void visitWhile(final ClassDeclaration object, final ClassMethodDeclaration method,
      final StmtWhile whileStmt) {
    applyExpression(object, whileStmt.getCondition());
    visitBlock(object, method, whileStmt.getBlock());
  }

  private void visitSelectionStmt(final ClassDeclaration object, final ClassMethodDeclaration method,
      final StmtSelect ifStmt) {
    applyExpression(object, ifStmt.getCondition());
    visitBlock(object, method, ifStmt.getTrueStatement());
    visitBlock(object, method, ifStmt.getOptionalElseStatement());

    if (!ifStmt.getCondition().getResultType().is_boolean()) {
      throw new AstParseException("if condition must be only a boolean type");
    }
  }

  private void visitForeach(final ClassDeclaration object, final ClassMethodDeclaration method,
      final StmtStatement statement) {
    throw new AstParseException("unimpl. foreach loop");
  }

  private void visitBlock(final ClassDeclaration object, final ClassMethodDeclaration method, final StmtBlock block) {

    symtabApplier.openBlockScope("block");

    for (StmtBlockItem item : block.getBlockItems()) {
      visitLocalVar(object, item.getLocalVariable());
      applyStatement(object, method, item.getStatement());
    }

    symtabApplier.closeBlockScope();
  }

  private void visitLocalVar(final ClassDeclaration object, final VarDeclarator var) {
    if (var == null) {
      return;
    }

    symtabApplier.defineBlockVar(var);
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

}
