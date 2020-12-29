package njast.ast_nodes.expr;

public class Ternary {
  private final ExpressionNode cond;
  private final ExpressionNode branchTrue;
  private final ExpressionNode branchFalse;

  public Ternary(ExpressionNode cond, ExpressionNode branchTrue, ExpressionNode branchFalse) {
    this.cond = cond;
    this.branchTrue = branchTrue;
    this.branchFalse = branchFalse;
  }

  public ExpressionNode getCond() {
    return cond;
  }

  public ExpressionNode getBranchTrue() {
    return branchTrue;
  }

  public ExpressionNode getBranchFalse() {
    return branchFalse;
  }

}
