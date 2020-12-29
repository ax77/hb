package njast.ast_nodes.expr;

import jscan.tokenize.Token;

public class Binary {
  private final Token operator;
  private final Expression lhs;
  private final Expression rhs;

  public Binary(Token operator, Expression lhs, Expression rhs) {
    this.operator = operator;
    this.lhs = lhs;
    this.rhs = rhs;
  }

  public Token getOperator() {
    return operator;
  }

  public Expression getLhs() {
    return lhs;
  }

  public Expression getRhs() {
    return rhs;
  }

}
