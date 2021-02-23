package ast_stmt;

import java.io.Serializable;

import ast_expr.ExprExpression;
import utils_oth.NullChecker;

public class StmtReturn implements Serializable {
  private static final long serialVersionUID = -3087335776553034864L;

  private ExprExpression expression;
  private final StmtBlock closestBlock;

  public StmtReturn(StmtBlock closestBlock) {
    NullChecker.check(closestBlock);
    this.closestBlock = closestBlock;
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

  public StmtBlock getClosestBlock() {
    return closestBlock;
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
