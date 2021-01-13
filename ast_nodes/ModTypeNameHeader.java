package njast.ast_nodes;

import java.io.Serializable;

import jscan.symtab.Ident;
import njast.modifiers.Modifiers;
import njast.parse.NullChecker;
import njast.templates.TypeSetter;
import njast.types.Type;

public class ModTypeNameHeader implements Serializable, TypeSetter, IModTypeNameHeader {
  private static final long serialVersionUID = 181348073850403240L;

  // these have similarly base, and we can move the header to special class:
  // 
  // method parameter
  // variable header
  // method header

  // var counter: int = 0;
  // let counter: int = 0;
  // weak var prev: Node<T>;

  private final Modifiers modifiers;
  private Type type; // we'll set new type in template expansion
  private final Ident identifier;

  public ModTypeNameHeader(Modifiers modifiers, Type type, Ident identifier) {
    NullChecker.check(modifiers, type, identifier);

    this.modifiers = modifiers;
    this.type = type;
    this.identifier = identifier;
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
  public Ident getIdentifier() {
    return identifier;
  }

  public Modifiers getModifiers() {
    return modifiers;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    final String mods = modifiers.toString();
    if (!mods.isEmpty()) {
      sb.append(mods);
      sb.append(" ");
    }

    sb.append(identifier.getName());
    sb.append(": ");
    sb.append(type.toString());

    return sb.toString();
  }

}
