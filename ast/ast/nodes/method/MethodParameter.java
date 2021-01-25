package ast.ast.nodes.method;

import java.io.Serializable;

import ast.templates.TypeSetter;
import ast.types.Type;
import jscan.tokenize.Ident;

public class MethodParameter implements Serializable, TypeSetter {
  private static final long serialVersionUID = 2569696266505848857L;

  private final Ident name;
  private /*final*/ Type type;

  public MethodParameter(Ident name, Type type) {
    this.name = name;
    this.type = type;
  }

  public Ident getName() {
    return name;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public void setType(Type typeToSet) {
    this.type = typeToSet;
  }

  @Override
  public String toString() {
    return name.getName() + ": " + type.toString();
  }

}
