package ast_mir;

import static ast_expr.ExpressionBase.EPRIMARY_IDENT;

import ast_expr.ExprExpression;
import ast_expr.ExpressionBase;
import errors.AstParseException;

public abstract class Lvalue {

  public static void checkHard(ExprExpression e) {
    final boolean isArrayAccess = e.is(ExpressionBase.EARRAY_ACCESS);
    final boolean isAnyIdentifier = e.is(EPRIMARY_IDENT);
    final boolean isFieldAccess = e.is(ExpressionBase.EFIELD_ACCESS);
    final boolean isLvalue = isArrayAccess || isAnyIdentifier || isFieldAccess;
    if (!isLvalue) {
      throw new AstParseException("not an lvalue: " + e.toString());
    }
  }

}
