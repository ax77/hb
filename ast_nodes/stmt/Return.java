package njast.ast_nodes.stmt;

import njast.ast_nodes.expr.ExpressionNode;

public class Return {
  private final ExpressionNode expr;

  public Return(ExpressionNode expr) {
    this.expr = expr;
  }

  public ExpressionNode getExpr() {
    return expr;
  }

}
