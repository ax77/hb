package ast_st2_annotate;

import static ast_expr.ExpressionBase.EPRIMARY_IDENT;

import ast_expr.ExprExpression;
import ast_expr.ExpressionBase;
import errors.ErrorLocation;

public abstract class LvalueUtil {

  public static void checkHard(ExprExpression e) {
    final boolean isAnyIdentifier = e.is(EPRIMARY_IDENT);
    final boolean isFieldAccess = e.is(ExpressionBase.EFIELD_ACCESS);
    final boolean isLvalue = isAnyIdentifier || isFieldAccess;
    if (!isLvalue) {
      ErrorLocation.errorExpression("not an lvalue", e);
    }
  }

}
