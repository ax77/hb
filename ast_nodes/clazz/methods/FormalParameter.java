package njast.ast_nodes.clazz.methods;

import jscan.symtab.Ident;
import njast.types.Type;

public class FormalParameter {

  private final Type type;
  private final Ident name;

  public FormalParameter(Type type, Ident name) {
    this.type = type;
    this.name = name;
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
