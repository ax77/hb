package ast_expr;

import ast_types.Type;

public class ExprCast {
  private final Type toType;
  private final ExprExpression expressionForCast;

  public ExprCast(Type toType, ExprExpression expressionForCast) {
    this.toType = toType;
    this.expressionForCast = expressionForCast;
  }

  public Type getToType() {
    return toType;
  }

  public ExprExpression getExpressionForCast() {
    return expressionForCast;
  }

  @Override
  public String toString() {
    return "cast(" + expressionForCast.toString() + ": " + toType.toString() + ")";
  }

}
