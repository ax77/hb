package njast.ast_nodes.clazz.vars;

import java.io.Serializable;

import jscan.symtab.Ident;
import njast.IModTypeNameHeader;
import njast.ModTypeNameHeader;
import njast.types.Type;

public class VarDeclarator implements Serializable, IModTypeNameHeader {
  private static final long serialVersionUID = -364976996504280849L;

  private final VarBase base;
  private final ModTypeNameHeader header;
  private VarInitializer initializer;

  public VarDeclarator(VarBase base, ModTypeNameHeader header) {
    this.base = base;
    this.header = header;
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

  @Override
  public String getLocationToString() {
    return header.getLocationToString();
  }

  public ModTypeNameHeader getHeader() {
    return header;
  }

}
