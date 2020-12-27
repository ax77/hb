package njast.ast_flow.expr;

import jscan.tokenize.Token;

public class Eselect {
  private final CExpression primary;
  private final Token identifier;

  public Eselect(CExpression primary, Token identifier) {
    this.primary = primary;
    this.identifier = identifier;
  }

  public CExpression getPrimary() {
    return primary;
  }

  public Token getIdentifier() {
    return identifier;
  }

}
