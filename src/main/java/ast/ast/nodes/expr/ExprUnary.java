package ast.ast.nodes.expr;

import jscan.tokenize.Token;

public class ExprUnary {

  private final Token operator;
  private final ExprExpression operand;

  public ExprUnary(Token operator, ExprExpression operand) {
    this.operator = operator;
    this.operand = operand;
  }

  public Token getOperator() {
    return operator;
  }

  public ExprExpression getOperand() {
    return operand;
  }

}
