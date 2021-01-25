package ast_expr;

import java.io.Serializable;

import ast_templates.TypeSetter;
import ast_types.Type;

public class ExprArrayCreation implements Serializable, TypeSetter {
  private static final long serialVersionUID = 3782365086790137846L;

  private Type arrayType;

  public ExprArrayCreation(Type arrayType) {
    this.arrayType = arrayType;
  }

  @Override
  public void setType(Type typeToSet) {
    this.arrayType = typeToSet;
  }

  @Override
  public Type getType() {
    return arrayType;
  }

  @Override
  public String toString() {
    return "new " + arrayType.toString();
  }

}
