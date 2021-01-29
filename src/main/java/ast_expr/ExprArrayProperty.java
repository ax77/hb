package ast_expr;

import tokenize.Ident;

public class ExprArrayProperty {
  private final ExprExpression array;
  private final Ident property;

  public ExprArrayProperty(ExprExpression array, Ident property) {
    this.array = array;
    this.property = property;
  }

  public ExprExpression getArray() {
    return array;
  }

  public Ident getProperty() {
    return property;
  }

  @Override
  public String toString() {
    return array.toString() + "." + property.getName();
  }

}
