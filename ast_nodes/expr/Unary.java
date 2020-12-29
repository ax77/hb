package njast.ast_nodes.expr;

import jscan.tokenize.Token;

public class Unary {
  private final Token operator;
  private final Expression operand;

  public Unary(Token operator, Expression operand) {
    this.operator = operator;
    this.operand = operand;
  }

  public Token getOperator() {
    return operator;
  }

  public Expression getOperand() {
    return operand;
  }

}
