package ast_expr;

import java.io.Serializable;

import tokenize.Token;

public class ExprUnary implements Serializable {
  private static final long serialVersionUID = -3984272729301362548L;

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

  @Override
  public String toString() {
    return operator.getValue() + operand.toString();
  }

}
