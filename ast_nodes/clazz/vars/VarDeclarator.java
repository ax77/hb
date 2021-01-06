package njast.ast_nodes.clazz.vars;

import java.io.Serializable;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import njast.modifiers.Modifiers;
import njast.parse.ILocation;
import njast.templates.TypeSetter;
import njast.types.Type;

public class VarDeclarator implements ILocation, Serializable, TypeSetter {
  private static final long serialVersionUID = -364976996504280849L;

  private Modifiers modifiers;
  private final VarBase base;
  private final SourceLocation location;
  private Type type;
  private final Ident identifier;
  private VarInitializer initializer;

  public VarDeclarator(VarBase base, SourceLocation location, Type type, Ident identifier) {
    this.base = base;
    this.location = location;
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

    sb.append(type.toString());
    sb.append(" ");
    sb.append(identifier.getName());

    if (initializer != null) {
      sb.append(" = ");
      sb.append(initializer.toString());
    }

    sb.append(";");
    return sb.toString();
  }

  @Override
  public SourceLocation getLocation() {
    return location;
  }

  @Override
  public String getLocationToString() {
    return location.toString();
  }

  @Override
  public int getLocationLine() {
    return location.getLine();
  }

  @Override
  public int getLocationColumn() {
    return location.getColumn();
  }

  @Override
  public String getLocationFile() {
    return location.getFilename();
  }

  public Modifiers getModifiers() {
    return modifiers;
  }

  public void setModifiers(Modifiers modifiers) {
    this.modifiers = modifiers;
  }

  public VarBase getBase() {
    return base;
  }

}
