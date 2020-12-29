package njast.ast_nodes.expr;

public class Ternary {
  private final Expression cond;
  private final Expression branchTrue;
  private final Expression branchFalse;

  public Ternary(Expression cond, Expression branchTrue, Expression branchFalse) {
    this.cond = cond;
    this.branchTrue = branchTrue;
    this.branchFalse = branchFalse;
  }

  public Expression getCond() {
    return cond;
  }

  public Expression getBranchTrue() {
    return branchTrue;
  }

  public Expression getBranchFalse() {
    return branchFalse;
  }

}
