package njast.ast_nodes.clazz.vars;

import jscan.symtab.Ident;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;
import njast.modifiers.Modifiers;
import njast.types.Type;

public class VarDeclarator implements AstTraverser {
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

  private final Type type;
  private final Ident identifier; // njast:mark - symbol instead ident?
  private VarInitializer initializer;

  public VarDeclarator(Type type, Ident identifier) {
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
    return "VarDeclarator [type=" + type + ", identifier=" + identifier.getName() + ", initializer="
        + ((initializer != null) ? initializer.getInitializer().toString() : "no_init") + "]";
  }

}
