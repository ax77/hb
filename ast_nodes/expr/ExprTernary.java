package njast.ast_nodes.expr;

import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class ExprTernary implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

  private final ExprExpression cond;
  private final ExprExpression branchTrue;
  private final ExprExpression branchFalse;

  public ExprTernary(ExprExpression cond, ExprExpression branchTrue, ExprExpression branchFalse) {
    this.cond = cond;
    this.branchTrue = branchTrue;
    this.branchFalse = branchFalse;
  }

  public ExprExpression getCond() {
    return cond;
  }

  public ExprExpression getBranchTrue() {
    return branchTrue;
  }

  public ExprExpression getBranchFalse() {
    return branchFalse;
  }

}
