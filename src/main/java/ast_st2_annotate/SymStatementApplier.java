package ast_st2_annotate;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_method.ClassMethodDeclaration;
import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
import ast_stmt.StmtForeach;
import ast_stmt.StmtSelect;
import ast_stmt.StmtStatement;
import ast_stmt.StmtWhile;
import ast_vars.VarDeclarator;
import errors.AstParseException;

public class SymStatementApplier {

  private final SymbolTable symtabApplier;

  public SymStatementApplier(SymbolTable symtabApplier) {
    this.symtabApplier = symtabApplier;
  }

  public void applyStatement(final ClassDeclaration object, final ClassMethodDeclaration method,
      final StmtStatement statement) {

    if (statement == null) {
      return;
    }

    StatementBase base = statement.getBase();
    if (base == StatementBase.SFOREACH_TMP) {
      visitForLoop(object, method, statement);
    } else if (base == StatementBase.SIF) {
      visitSelectionStmt(object, method, statement);
    } else if (base == StatementBase.SEXPR) {
      applyExpression(object, statement.getExprStmt());
    } else if (base == StatementBase.SBLOCK) {
      visitBlock(object, method, statement.getBlockStmt());
    } else if (base == StatementBase.SRETURN) {
      applyExpression(object, statement.getExprStmt());
    } else if (base == StatementBase.SWHILE) {
      visitWhile(object, method, statement);
    } else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }

  }

  private void visitWhile(final ClassDeclaration object, final ClassMethodDeclaration method,
      final StmtStatement statement) {
    final StmtWhile whileStmt = statement.getWhileStmt();
    applyExpression(object, whileStmt.getCondition());
    visitBlock(object, method, whileStmt.getBlock());
  }

  private void visitSelectionStmt(final ClassDeclaration object, final ClassMethodDeclaration method,
      final StmtStatement statement) {
    final StmtSelect ifStmt = statement.getIfStmt();
    applyExpression(object, ifStmt.getCondition());
    applyStatement(object, method, ifStmt.getTrueStatement());
    applyStatement(object, method, ifStmt.getOptionalElseStatement());

    if (!ifStmt.getCondition().getResultType().is_boolean()) {
      throw new AstParseException("if condition must be only a boolean type");
    }
  }

  private void visitForLoop(final ClassDeclaration object, final ClassMethodDeclaration method,
      final StmtStatement statement) {
    final StmtForeach forloop = statement.getForStmt();

    final ForeachToWhileRewriter rewriter = new ForeachToWhileRewriter(symtabApplier, object, forloop);
    final StmtBlock resultBlock = rewriter.genBlock();

    statement.replaceForLoopWithBlock(resultBlock);
    applyStatement(object, method, statement);
  }

  private void visitBlock(final ClassDeclaration object, final ClassMethodDeclaration method, final StmtBlock body) {

    symtabApplier.openBlockScope("block");

    for (StmtBlockItem block : body.getBlockStatements()) {
      visitLocalVar(object, block.getLocalVariable());
      applyStatement(object, method, block.getStatement());
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
    SymExpressionApplier applier = new SymExpressionApplier(symtabApplier);
    applier.applyExpression(object, e);
  }

  private void applyInitializer(ClassDeclaration object, VarDeclarator var) {
    SymInitializerApplier applier = new SymInitializerApplier(symtabApplier);
    applier.applyInitializer(object, var);

  }

}