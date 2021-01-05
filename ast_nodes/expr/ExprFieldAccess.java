package njast.ast_nodes.expr;

import java.io.Serializable;

import jscan.symtab.Ident;

public class ExprFieldAccess implements Serializable {
  private static final long serialVersionUID = -6528124385251141959L;
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
