package ast_expr;

import java.io.Serializable;

public class ExprForLoopStepComma implements Serializable {
  private static final long serialVersionUID = 7441206591539888580L;
  private final ExprExpression lhs;
  private final ExprExpression rhs;

  public ExprForLoopStepComma(ExprExpression lhs, ExprExpression rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
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
    sb.append(", ");
    sb.append(rhs.toString());
    return sb.toString();
  }

}
