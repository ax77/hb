package njast.ast_nodes.expr;

import jscan.symtab.Ident;
import njast.ast_visitors.Symbol;

public class ExprPrimaryIdent {
  private final Ident identifier;
  private Symbol sym; // MARK:HIR

  public ExprPrimaryIdent(Ident identifier) {
    this.identifier = identifier;
  }

  public Symbol getSym() {
    return sym;
  }

  public void setSym(Symbol sym) {
    this.sym = sym;
  }

  public Ident getIdentifier() {
    return identifier;
  }

}
