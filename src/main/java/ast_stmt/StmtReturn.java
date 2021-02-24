package ast_stmt;

import java.io.Serializable;

import ast_expr.ExprExpression;

public class StmtReturn implements Serializable {
  private static final long serialVersionUID = -3087335776553034864L;

  private ExprExpression expression;

  public StmtReturn() {
  }

  public ExprExpression getExpression() {
    return expression;
  }

  public void setExpression(ExprExpression expression) {
    this.expression = expression;
  }

  public boolean hasExpression() {
    return expression != null;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("return");
    if (hasExpression()) {
      sb.append(" ");
      sb.append(expression.toString());
    }
    sb.append(";");
    return sb.toString();
  }

}
