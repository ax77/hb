package njast.ast_nodes.expr;

public class ExprTernary {

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
