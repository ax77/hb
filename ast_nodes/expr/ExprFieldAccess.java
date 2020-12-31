package njast.ast_nodes.expr;

import jscan.symtab.Ident;

public class ExprFieldAccess {
  private final Ident fieldName;
  private final ExprExpression object;

  public ExprFieldAccess(Ident fieldName, ExprExpression object) {
    this.fieldName = fieldName;
    this.object = object;
  }

  public Ident getFieldName() {
    return fieldName;
  }

  public ExprExpression getObject() {
    return object;
  }

  @Override
  public String toString() {
    return object.toString() + "." + fieldName.getName();
  }

}
