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
      visitExprStmt(object, s);
    } else if (base == StatementBase.SBLOCK) {
      visitBlock(object, method, s.getBlockStmt());
    } else if (base == StatementBase.SRETURN) {
      visitReturn(object, s.getReturnStmt());
    } else if (base == StatementBase.SWHILE) {
      visitWhile(object, method, s.getWhileStmt());
    } else if (base == StatementBase.SFOR) {
      visitFor(object, method, s.getForStmt());
    } else if (base == StatementBase.SBREAK || base == StatementBase.SCONTINUE) {
      //TODO:
    } else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }

  }

  private void visitExprStmt(final ClassDeclaration object, final StmtStatement node) {
    applyExpression(object, node.getExprStmt());
    semExprStmt(node);
  }

  private void visitReturn(final ClassDeclaration object, final StmtReturn node) {
    applyExpression(object, node.getExpression());
    semReturn(node);
  }

  private void visitFor(ClassDeclaration object, ClassMethodDeclaration method, StmtFor node) {

    symtabApplier.openBlockScope("block", node.getBlock());
    visitLocalVar(object, node.getDecl());
    applyExpression(object, node.getInit());
    applyExpression(object, node.getTest());
    applyExpression(object, node.getStep());
    visitBlock(object, method, node.getBlock());
    symtabApplier.closeBlockScope();

    semFor(node);
  }

  private void visitWhile(final ClassDeclaration object, final ClassMethodDeclaration method, final StmtWhile node) {
    applyExpression(object, node.getCondition());
    visitBlock(object, method, node.getBlock());
    semWhile(node);
  }

  private void visitSelectionStmt(final ClassDeclaration object, final ClassMethodDeclaration method,
      final StmtSelect node) {
    applyExpression(object, node.getCondition());
    visitBlock(object, method, node.getTrueStatement());
    visitBlock(object, method, node.getOptionalElseStatement());
    semSelection(node);
  }

  private void visitBlock(final ClassDeclaration object, final ClassMethodDeclaration method, final StmtBlock block) {

    symtabApplier.openBlockScope("block", block);

    for (StmtBlockItem item : block.getBlockItems()) {
      visitLocalVar(object, item.getLocalVariable());
      applyStatement(object, method, item.getStatement());
      semBlockItem(item);
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

  /// 3ac-semantic

  private void semExprStmt(final StmtStatement node) {
    node.setLinearExprStmt(GetCodeItems.getFlatCode(node.getExprStmt()));
  }

  private void semBlockItem(StmtBlockItem item) {
    item.setLinearLocalVariable(GetCodeItems.getFlatCode(item.getLocalVariable()));
  }

  private void semWhile(final StmtWhile node) {
    checkIsBoolean(node.getCondition());
    node.setLinearCondition(GetCodeItems.getFlatCode(node.getCondition()));
  }

  private void semReturn(final StmtReturn node) {
    node.setLinearExpression(GetCodeItems.getFlatCode(node.getExpression()));
    symtabApplier.peekBlock().addReturn(node);
  }

  private void semFor(StmtFor node) {
    checkIsBoolean(node.getTest());

    node.setLinearDecl(GetCodeItems.getFlatCode(node.getDecl()));
    node.setLinearInit(GetCodeItems.getFlatCode(node.getInit()));
    node.setLinearTest(GetCodeItems.getFlatCode(node.getTest()));
    node.setLinearStep(GetCodeItems.getFlatCode(node.getStep()));
  }

  private void semSelection(final StmtSelect node) {
    checkIsBoolean(node.getCondition());
    node.setLinearCondition(GetCodeItems.getFlatCode(node.getCondition()));
  }

  private void checkIsBoolean(ExprExpression e) {
    // that's ok - the test expression of the for-loop must ne null
    // for example.
    if (e == null) {
      return;
    }
    if (!e.getResultType().is_boolean()) {
      ErrorLocation.errorExpression("expected boolean type: ", e);
    }
  }

}
