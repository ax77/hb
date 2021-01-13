package njast.ast_nodes.clazz.vars;

import java.io.Serializable;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import njast.ast_nodes.IModTypeNameHeader;
import njast.ast_nodes.ModTypeNameHeader;
import njast.parse.ILocation;
import njast.types.Type;

public class VarDeclarator implements Serializable, IModTypeNameHeader, ILocation {
  private static final long serialVersionUID = -364976996504280849L;

  private final VarBase base;
  private final ModTypeNameHeader header;
  private VarInitializer initializer;
  private final SourceLocation location;

  public VarDeclarator(VarBase base, ModTypeNameHeader header, SourceLocation location) {
    this.base = base;
    this.header = header;
    this.location = location;
  }

  public VarInitializer getInitializer() {
    return initializer;
  }

  public void setInitializer(VarInitializer initializer) {
    this.initializer = initializer;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(header.toString());
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
  public Type getType() {
    return header.getType();
  }

  @Override
  public Ident getIdentifier() {
    return header.getIdentifier();
  }

  public ModTypeNameHeader getHeader() {
    return header;
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
