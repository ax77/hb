package ast.ast.nodes.expr;

import java.io.Serializable;

import jscan.tokenize.Token;

public class ExprBinary implements Serializable {
  private static final long serialVersionUID = 7441206591539888580L;
  private final Token operator;
  private final ExprExpression lhs;
  private final ExprExpression rhs;

  public ExprBinary(Token operator, ExprExpression lhs, ExprExpression rhs) {
    this.operator = operator;
    this.lhs = lhs;
    this.rhs = rhs;
  }

  public Token getOperator() {
    return operator;
  }

  public ExprExpression getLhs() {
    return lhs;
  }

  public ExprExpression getRhs() {
    return rhs;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(lhs.toString());
    sb.append(" ");
    sb.append(operator.getValue());
    sb.append(" ");
    sb.append(rhs.toString());
    return sb.toString();
  }

}
