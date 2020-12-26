package njast.ast_class.vars;

import jscan.symtab.Ident;

public class VariableDeclarator {

  //  <variable declarator> ::= <variable declarator id> | <variable declarator id> = <variable initializer>
  //
  //  <variable declarator id> ::= <identifier> | <variable declarator id> [ ]
  //
  //  <variable initializer> ::= <expression> | <array initializer>

  private final Ident identifier; // njast:mark - symbol instead ident?
  private VarialbeInitializer initializer;

  public VariableDeclarator(Ident identifier) {
    this.identifier = identifier;
  }

}
