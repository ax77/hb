package njast.ast_class.vars;

import jscan.symtab.Ident;

public class VariableDeclarator {

  //  <variable declarator> ::= <variable declarator id> | <variable declarator id> = <variable initializer>
  //
  //  <variable declarator id> ::= <identifier> | <variable declarator id> [ ]
  //
  //  <variable initializer> ::= <expression> | <array initializer>

  private Ident variableDeclaratorId; // njast:mark - symbol instead ident?
  private VarialbeInitializer varialbeInitializer;

  public VariableDeclarator() {
  }

  public VariableDeclarator(Ident variableDeclaratorId) {
    this.variableDeclaratorId = variableDeclaratorId;
  }

  public VariableDeclarator(Ident variableDeclaratorId, VarialbeInitializer varialbeInitializer) {
    this.variableDeclaratorId = variableDeclaratorId;
    this.varialbeInitializer = varialbeInitializer;
  }

  public Ident getVariableDeclaratorId() {
    return variableDeclaratorId;
  }

  public void setVariableDeclaratorId(Ident variableDeclaratorId) {
    this.variableDeclaratorId = variableDeclaratorId;
  }

  public VarialbeInitializer getVarialbeInitializer() {
    return varialbeInitializer;
  }

  public void setVarialbeInitializer(VarialbeInitializer varialbeInitializer) {
    this.varialbeInitializer = varialbeInitializer;
  }

}
