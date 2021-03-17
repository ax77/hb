package ast_expr;

import java.io.Serializable;

import _st1_templates.TypeSetter;
import ast_types.Type;

public class ExprSizeof implements Serializable, TypeSetter {
  private static final long serialVersionUID = 4850229256121198539L;

  private Type type;

  public ExprSizeof(Type type) {
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
    return "sizeof(" + type.toString() + ")";
  }

}
