package ast_expr;

import java.io.Serializable;

import _st1_templates.TypeSetter;
import ast_types.Type;

public class ExprTypeof implements Serializable, TypeSetter {
  private static final long serialVersionUID = 4850229256121198539L;

  private final ExprExpression expr;
  private Type type;

  public ExprTypeof(ExprExpression expr, Type type) {
    this.expr = expr;
    this.type = type;
  }

  public ExprExpression getExpr() {
    return expr;
  }

  @Override
  public void setType(Type typeToSet) {
    this.type = typeToSet;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public String toString() {
    return "typeof(" + expr.toString() + ": " + type.toString() + ")";
  }

}
