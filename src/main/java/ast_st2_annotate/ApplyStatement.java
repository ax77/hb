package ast_st2_annotate;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_expr.ExpressionBase;
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
      final StmtStatement statement) {

    if (statement == null) {
      return;
    }

    StatementBase base = statement.getBase();
    if (base == StatementBase.SFOREACH_TMP) {
      visitForeach(object, method, statement);
    } else if (base == StatementBase.SIF) {
      visitSelectionStmt(object, method, statement);
    } else if (base == StatementBase.SEXPR) {
      applyExpression(object, statement.getExprStmt());
    } else if (base == StatementBase.SBLOCK) {
      visitBlock(object, method, statement.getBlockStmt());
    } else if (base == StatementBase.SRETURN) {
      applyReturn(object, statement.getReturnStmt());
    } else if (base == StatementBase.SWHILE) {
      visitWhile(object, method, statement);
    } else if (base == StatementBase.SFOR) {
      applyFor(object, method, statement.getForStmt());
    } else {
      throw new AstParseException("unimpl. stmt.:" + base.toString());
    }

  }

  private void applyReturn(ClassDeclaration object, StmtReturn returnStmt) {
    applyExpression(object, returnStmt.getExpression());

    // bind variables
    if (returnStmt.hasExpression()) {
      List<Symbol> vars = new ArrayList<>();
      applyExpressionSimple(returnStmt.getExpression(), vars);
      for (Symbol sym : vars) {
        if (sym.isVariable()) {
          /// if it is not a var - it may be a static var from abstract class
          /// and it is not interesting, we do not process static vars
          /// nevertheless
          returnStmt.registerVariable(sym.getVariable());
        }
      }
    }
  }

  public void applyExpressionSimple(ExprExpression e, List<Symbol> vars) {

    if (e == null) {
      return;
    }
    if (e.is(ExpressionBase.EUNARY)) {
      applyExpressionSimple(e.getUnary().getOperand(), vars);
    } else if (e.is(ExpressionBase.EBINARY)) {
      applyExpressionSimple(e.getBinary().getLhs(), vars);
      applyExpressionSimple(e.getBinary().getRhs(), vars);
    } else if (e.is(ExpressionBase.EASSIGN)) {
      applyExpressionSimple(e.getAssign().getLvalue(), vars);
      applyExpressionSimple(e.getAssign().getRvalue(), vars);
    } else if (e.is(ExpressionBase.EPRIMARY_IDENT)) {
      vars.add(e.getIdent().getSym());
    } else if (e.is(ExpressionBase.EMETHOD_INVOCATION)) {
      applyExpressionSimple(e.getMethodInvocation().getObject(), vars);
      for (ExprExpression arg : e.getMethodInvocation().getArguments()) {
        applyExpressionSimple(arg, vars);
      }
    } else if (e.is(ExpressionBase.EFIELD_ACCESS)) {
      applyExpressionSimple(e.getFieldAccess().getObject(), vars);
    } else if (e.is(ExpressionBase.ETHIS)) {
    } else if (e.is(ExpressionBase.EPRIMARY_NUMBER)) {
    } else if (e.is(ExpressionBase.EPRIMARY_NULL_LITERAL)) {
    } else if (e.is(ExpressionBase.ECLASS_INSTANCE_CREATION)) {
      for (ExprExpression arg : e.getClassCreation().getArguments()) {
        applyExpressionSimple(arg, vars);
      }
    } else if (e.is(ExpressionBase.ESTRING_CONST)) {
    } else if (e.is(ExpressionBase.ECHAR_CONST)) {
    } else if (e.is(ExpressionBase.EBOOLEAN_LITERAL)) {
    } else if (e.is(ExpressionBase.ECAST)) {
    } else if (e.is(ExpressionBase.EBUILTIN_FN)) {
    } else {
      ErrorLocation.errorExpression("unimpl.expression-type-applier", e);
    }

  }

  private void applyFor(ClassDeclaration object, ClassMethodDeclaration method, StmtFor stmtFor) {

    symtabApplier.openBlockScope("block");
    visitLocalVar(object, stmtFor.getDecl());

    applyExpression(object, stmtFor.getInit());
    applyExpression(object, stmtFor.getTest());
    applyExpression(object, stmtFor.getStep());
    applyStatement(object, method, stmtFor.getLoop());

    symtabApplier.closeBlockScope();
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

  private void visitForeach(final ClassDeclaration object, final ClassMethodDeclaration method,
      final StmtStatement statement) {

    throw new AstParseException("unimpl. foreach loop");

    // final StmtForeach forloop = statement.getForeachStmt();
    // 
    // final ForeachToWhileRewriter rewriter = new ForeachToWhileRewriter(symtabApplier, object, forloop);
    // final StmtBlock resultBlock = rewriter.genBlock();
    // 
    // statement.replaceForLoopWithBlock(resultBlock);
    // applyStatement(object, method, statement);
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
    ApplyExpression applier = new ApplyExpression(symtabApplier);
    applier.applyExpression(object, e);
  }

  private void applyInitializer(ClassDeclaration object, VarDeclarator var) {
    ApplyInitializer applier = new ApplyInitializer(symtabApplier);
    applier.applyInitializer(object, var);

  }

}
