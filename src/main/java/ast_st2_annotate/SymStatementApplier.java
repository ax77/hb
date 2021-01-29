package ast_st2_annotate;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_method.ClassMethodDeclaration;
import ast_stmt.StatementBase;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
import ast_stmt.StmtStatement;
import ast_stmt.Stmt_for;
import ast_stmt.Stmt_if;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import errors.ErrorLocation;

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
    if (base == StatementBase.SFOR) {
      visitForLoop(object, method, statement);
    } else if (base == StatementBase.SIF) {
      visit_if(object, method, statement);
    } else if (base == StatementBase.SEXPR) {
      applyExpression(object, statement.getExprStmt());
    } else if (base == StatementBase.SBLOCK) {
      visitBlock(object, method, statement.getBlockStmt());
    } else if (base == StatementBase.SRETURN) {
      applyExpression(object, statement.getExprStmt());
    } else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }

  }

  private void visit_if(final ClassDeclaration object, final ClassMethodDeclaration method,
      final StmtStatement statement) {
    Stmt_if sif = statement.getIfStmt();
    applyExpression(object, sif.getCondition());
    applyStatement(object, method, sif.getTrueStatement());
    applyStatement(object, method, sif.getOptionalElseStatement());

    if (!sif.getCondition().getResultType().is_boolean()) {
      throw new AstParseException("if condition must be only a boolean type");
    }
  }

  private void visitForLoop(final ClassDeclaration object, final ClassMethodDeclaration method,
      final StmtStatement statement) {
    final Stmt_for forloop = statement.getForStmt();

    if (forloop.isShortForm()) {

      // 1)
      final ExprExpression collection = forloop.getAuxCollection();
      applyExpression(object, collection);

      // 2)
      ForLoopRewriter.rewriteForLoop(object, forloop);

      // 3) normal for-loop here, in its pure-huge form

      final List<StmtBlockItem> items = new ArrayList<>();
      final List<VarDeclarator> decl = forloop.getDecl();
      if (decl.size() != 2) {
        ErrorLocation.errorExpression("for-loop rewriter aux error", collection);
      }

      items.add(new StmtBlockItem(decl.remove(0)));
      forloop.getLoop().getBlockStatements().add(0, new StmtBlockItem(decl.remove(0))); // TODO:

      forloop.setDecl(null);
      forloop.setShortForm(false);

      items.add(new StmtBlockItem(new StmtStatement(forloop, collection.getBeginPos())));
      final StmtBlock block = new StmtBlock(items);

      statement.replaceForLoopWithBlock(block);
      applyStatement(object, method, statement);

    }

    if (forloop.getDecl() != null) {
      for (VarDeclarator var : forloop.getDecl()) {
        visitLocalVar(object, var);
      }
    }

    applyExpression(object, forloop.getTest());
    applyExpression(object, forloop.getStep());
    visitBlock(object, method, forloop.getLoop());
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
