package njast.ast_nodes.expr;

import jscan.symtab.Ident;
import njast.ast_visitors.Symbol;

public class ExprPrimaryIdent {
  private final Ident identifier;

  public ExprPrimaryIdent(Ident identifier) {
    this.identifier = identifier;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  @Override
  public String toString() {
    return identifier.toString();
  }
  
  

}
