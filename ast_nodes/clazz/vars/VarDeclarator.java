package njast.ast_nodes.clazz.vars;

import java.io.Serializable;

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
  private VarInitializer initializer;
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

  public VarInitializer getInitializer() {
    return initializer;
  }

  public void setInitializer(VarInitializer initializer) {
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
      sb.append(initializer.getInitializer().toString());
    }
    sb.append("; ");
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
