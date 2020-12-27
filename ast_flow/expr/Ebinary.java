package njast.ast_flow.expr;

import jscan.tokenize.Token;

public class Ebinary {
  private final Token operator;
  private final CExpression lhs;
  private final CExpression rhs;

  public Ebinary(Token operator, CExpression lhs, CExpression rhs) {
    this.operator = operator;
    this.lhs = lhs;
    this.rhs = rhs;
  }

  public Token getOperator() {
    return operator;
  }

  public CExpression getLhs() {
    return lhs;
  }

  public CExpression getRhs() {
    return rhs;
  }

}
