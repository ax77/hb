package njast.ast_nodes.clazz.methods;

import java.io.Serializable;

import jscan.symtab.Ident;
import njast.TypeSetter;
import njast.types.Type;

public class FormalParameter implements Serializable, TypeSetter {

  private static final long serialVersionUID = 8224274363196087023L;

  private /*final*/ Type type;
  private final Ident name;

  public FormalParameter(Type type, Ident name) {
    this.type = type;
    this.name = name;
  }

  @Override
  public void setType(Type type) {
    this.type = type;
  }

  public Type getType() {
    return type;
  }

  public Ident getName() {
    return name;
  }

  @Override
  public String toString() {
    return name + ": " + type.toString();
  }

}
