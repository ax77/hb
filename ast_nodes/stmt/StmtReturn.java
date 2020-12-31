package njast.ast_nodes.stmt;

import njast.ast_nodes.expr.ExprExpression;

public class StmtReturn {

  private final ExprExpression expr;

  public StmtReturn(ExprExpression expr) {
    this.expr = expr;
  }

  public ExprExpression getExpr() {
    return expr;
  }

}
