package njast.ast_nodes.expr;

import jscan.tokenize.Token;

public class Binary {
  private final Token operator;
  private final ExpressionNode lhs;
  private final ExpressionNode rhs;

  public Binary(Token operator, ExpressionNode lhs, ExpressionNode rhs) {
    this.operator = operator;
    this.lhs = lhs;
    this.rhs = rhs;
  }

  public Token getOperator() {
    return operator;
  }

  public ExpressionNode getLhs() {
    return lhs;
  }

  public ExpressionNode getRhs() {
    return rhs;
  }

}
