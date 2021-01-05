package njast.ast_nodes.clazz.methods;

import java.io.Serializable;

import jscan.symtab.Ident;
import njast.templates.TypeSetter;
import njast.types.Type;

public class FormalParameter implements Serializable, TypeSetter {

  private static final long serialVersionUID = 8224274363196087023L;

  private Type type;
  private final Ident identifier;

  public FormalParameter(Type type, Ident identifier) {
    this.type = type;
    this.identifier = identifier;
  }

  @Override
  public void setType(Type type) {
    this.type = type;
  }

  @Override
  public Type getType() {
    return type;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  @Override
  public String toString() {
    return type.toString() + " " + identifier.getName();
  }

}
