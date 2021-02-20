package ast_stmt;

import java.io.Serializable;

import ast_expr.ExprExpression;
import ast_st3_tac.FlatCode;

public class StmtReturn implements Serializable {
  private static final long serialVersionUID = -3087335776553034864L;

  private ExprExpression expression;

  /// 3ac
  private FlatCode linearExpression;

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

  public FlatCode getLinearExpression() {
    return linearExpression;
  }

  public void setLinearExpression(FlatCode linearExpression) {
    this.linearExpression = linearExpression;
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
