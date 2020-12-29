package njast.ast_nodes.expr;

import jscan.tokenize.Token;

public class Unary {
  private final Token operator;
  private final ExpressionNode operand;

  public Unary(Token operator, ExpressionNode operand) {
    this.operator = operator;
    this.operand = operand;
  }

  public Token getOperator() {
    return operator;
  }

  public ExpressionNode getOperand() {
    return operand;
  }

}
