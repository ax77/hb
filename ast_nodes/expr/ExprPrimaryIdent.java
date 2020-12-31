package njast.ast_nodes.expr;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.vars.VarDeclarator;

public class ExprPrimaryIdent {
  private final Ident identifier;
  private VarDeclarator variable;

  public ExprPrimaryIdent(Ident identifier) {
    this.identifier = identifier;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public VarDeclarator getVariable() {
    return variable;
  }

  public void setVariable(VarDeclarator variable) {
    this.variable = variable;
  }

  @Override
  public String toString() {
    return identifier.toString();
  }

}
