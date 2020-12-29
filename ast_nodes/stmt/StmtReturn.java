package njast.ast_nodes.stmt;

import njast.ast_nodes.expr.ExprExpression;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class StmtReturn implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

  private final ExprExpression expr;

  public StmtReturn(ExprExpression expr) {
    this.expr = expr;
  }

  public ExprExpression getExpr() {
    return expr;
  }

}
