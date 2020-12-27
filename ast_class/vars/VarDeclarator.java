package njast.ast_class.vars;

import jscan.symtab.Ident;

public class VarDeclarator {

  //  <variable declarator> ::= <variable declarator id> | <variable declarator id> = <variable initializer>
  //
  //  <variable declarator id> ::= <identifier> | <variable declarator id> [ ]
  //
  //  <variable initializer> ::= <expression> | <array initializer>

  private final Ident identifier; // njast:mark - symbol instead ident?
  private VarInitializer initializer;

  public VarDeclarator(Ident identifier) {
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

}
