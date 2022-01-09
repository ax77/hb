package _st2_annotate;

import static ast_expr.ExpressionBase.EPRIMARY_IDENT;

import ast_expr.ExprExpression;
import ast_expr.ExpressionBase;
import ast_method.ClassMethodDeclaration;
import ast_vars.VarDeclarator;
import errors.ErrorLocation;

public abstract class LvalueUtil {

  public static void checkHard(ExprExpression lvalue, ExprExpression theWholeAssignExpression,
      ClassMethodDeclaration methodHolder) {
    final boolean isAnyIdentifier = lvalue.is(EPRIMARY_IDENT);
    final boolean isFieldAccess = lvalue.is(ExpressionBase.EFIELD_ACCESS);
    final boolean isLvalue = isAnyIdentifier || isFieldAccess;
    if (!isLvalue) {
      ErrorLocation.errorExpression("not an lvalue", theWholeAssignExpression);
    }

    if (!methodHolder.isConstructor() && !methodHolder.isDestructor()) {
      if (isAnyIdentifier) {
        VarDeclarator var = lvalue.getIdent().getVar();
        if (!var.getMods().isMutable()) {
          ErrorLocation.errorExpression("const variable is not assignable", theWholeAssignExpression);
        }
      }
      if (isFieldAccess) {
        VarDeclarator var = lvalue.getFieldAccess().getField();
        if (!var.getMods().isMutable()) {
          ErrorLocation.errorExpression("const variable is not assignable", theWholeAssignExpression);
        }
      }
    }
  }

}
