package njast.ast_nodes.clazz.vars;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;
import njast.modifiers.Modifiers;
import njast.parse.ILocation;
import njast.types.Type;

public class VarDeclarator implements AstTraverser, ILocation {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

  // var-declarator-list:
  // type:[int] var-declarators:[a, b=1, c, d=2];
  //
  // we simplify this in a parse stage:
  // parse type, and apply this type to each variable (i.e. name)
  // and we have a list of variables with its types as a result

  private Modifiers modifiers; // later

  private final VarBase base;
  private final SourceLocation location;
  private final Type type;
  private final Ident identifier; // njast:mark - symbol instead ident?
  private VarInitializer initializer;

  public VarDeclarator(VarBase base, SourceLocation location, Type type, Ident identifier) {
    this.base = base;
    this.location = location;
    this.type = type;
    this.identifier = identifier;
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

  public Type getType() {
    return type;
  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();

    sb.append(base.toString());
    sb.append(": ");
    sb.append(identifier.getName());
    sb.append(": ");
    sb.append(type.toString());

    if (initializer != null) {
      sb.append(" = ");
      sb.append(initializer.getInitializer().toString());
    } else {
      sb.append(" = <uninitialized>");
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
