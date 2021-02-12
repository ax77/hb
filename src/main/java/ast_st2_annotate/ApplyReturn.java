package ast_st2_annotate;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_expr.ExpressionBase;
import ast_stmt.StmtReturn;
import errors.ErrorLocation;

public class ApplyReturn {

  private final SymbolTable symtabApplier;

  public ApplyReturn(SymbolTable symtabApplier) {
    this.symtabApplier = symtabApplier;
  }

  public void applyReturn(ClassDeclaration object, StmtReturn returnStmt) {
    applyExpression(object, returnStmt.getExpression());

    // bind variables
    if (returnStmt.hasExpression()) {
      List<Symbol> vars = new ArrayList<>();
      walkExpressionSimple(returnStmt.getExpression(), vars);
      for (Symbol sym : vars) {
        if (sym.isVariable()) {
          /// if it is not a var - it may be a static var from abstract class
          /// and it is not interesting, we do not process static vars
          /// nevertheless
          returnStmt.registerVariable(sym.getVariable());
        }
      }
    }

    symtabApplier.getCurrentBlock().registerReturn(returnStmt);
  }

  public void walkExpressionSimple(ExprExpression e, List<Symbol> vars) {

    if (e == null) {
      return;
    }
    if (e.is(ExpressionBase.EUNARY)) {
      walkExpressionSimple(e.getUnary().getOperand(), vars);
    } else if (e.is(ExpressionBase.EBINARY)) {
      walkExpressionSimple(e.getBinary().getLhs(), vars);
      walkExpressionSimple(e.getBinary().getRhs(), vars);
    } else if (e.is(ExpressionBase.EASSIGN)) {
      walkExpressionSimple(e.getAssign().getLvalue(), vars);
      walkExpressionSimple(e.getAssign().getRvalue(), vars);
    } else if (e.is(ExpressionBase.EPRIMARY_IDENT)) {
      vars.add(e.getIdent().getSym());
    } else if (e.is(ExpressionBase.EMETHOD_INVOCATION)) {
      walkExpressionSimple(e.getMethodInvocation().getObject(), vars);
      for (ExprExpression arg : e.getMethodInvocation().getArguments()) {
        walkExpressionSimple(arg, vars);
      }
    } else if (e.is(ExpressionBase.EFIELD_ACCESS)) {
      walkExpressionSimple(e.getFieldAccess().getObject(), vars);
    } else if (e.is(ExpressionBase.ETHIS)) {
    } else if (e.is(ExpressionBase.EPRIMARY_NUMBER)) {
    } else if (e.is(ExpressionBase.EPRIMARY_NULL_LITERAL)) {
    } else if (e.is(ExpressionBase.ECLASS_INSTANCE_CREATION)) {
      for (ExprExpression arg : e.getClassCreation().getArguments()) {
        walkExpressionSimple(arg, vars);
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

  private void applyExpression(ClassDeclaration object, ExprExpression e) {
    ApplyExpression applier = new ApplyExpression(symtabApplier);
    applier.applyExpression(object, e);
  }

}
