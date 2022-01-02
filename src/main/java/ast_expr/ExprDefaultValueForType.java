package ast_expr;

import java.io.Serializable;

import _st1_templates.TypeSetter;
import ast_types.Type;

public class ExprDefaultValueForType implements Serializable, TypeSetter {
  private static final long serialVersionUID = 8201510472448396221L;

  private Type type;

  public ExprDefaultValueForType(Type type) {
    this.type = type;
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
    return "default(" + type.toString() + ")";
  }

}
