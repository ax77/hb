package njast.ast_flow.expr;

import jscan.tokenize.Token;

public class Eunary {
  private final Token operator;
  private final CExpression operand;

  public Eunary(Token operator, CExpression operand) {
    this.operator = operator;
    this.operand = operand;
  }

  public Token getOperator() {
    return operator;
  }

  public CExpression getOperand() {
    return operand;
  }

}
