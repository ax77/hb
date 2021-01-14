package njast.ast_nodes.clazz.vars;

import java.io.Serializable;
import java.util.List;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import njast.parse.ILocation;
import njast.templates.TypeSetter;
import njast.types.Type;

public class VarDeclarator implements Serializable, TypeSetter, ILocation {
  private static final long serialVersionUID = -364976996504280849L;

  private final VarBase base;
  private /*final*/ Type type;
  private final Ident identifier;
  private List<VarInitializer> initializer;
  private final SourceLocation location;

  public VarDeclarator(VarBase base, Type type, Ident identifier, SourceLocation location) {
    this.base = base;
    this.type = type;
    this.identifier = identifier;
    this.location = location;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public void setType(Type typeToSet) {
    this.type = typeToSet;
  }

  public List<VarInitializer> getInitializer() {
    return initializer;
  }

  public void setInitializer(List<VarInitializer> initializer) {
    this.initializer = initializer;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(identifier.getName());
    sb.append(": ");
    sb.append(type.toString());
    if (initializer != null) {
      sb.append(" = ");
      sb.append(inits(initializer));
    }
    sb.append("; ");
    return sb.toString();
  }

  private String inits(List<VarInitializer> inits) {
    StringBuilder sb = new StringBuilder();
    sb.append("[ ");
    for (int i = 0; i < inits.size(); i++) {
      VarInitializer init = inits.get(i);
      sb.append(init.toString());
      if (i + 1 < inits.size()) {
        sb.append(", ");
      }
    }
    sb.append("]");
    return sb.toString();
  }

  public VarBase getBase() {
    return base;
  }

  @Override
  public SourceLocation getLocation() {
    return location;
  }

  @Override
  public String getLocationToString() {
    return location.toString();
  }

}
