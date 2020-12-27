package njast.ast_flow.expr;

public class Eternary {
  private final CExpression cond;
  private final CExpression branchTrue;
  private final CExpression branchFalse;

  public Eternary(CExpression cond, CExpression branchTrue, CExpression branchFalse) {
    this.cond = cond;
    this.branchTrue = branchTrue;
    this.branchFalse = branchFalse;
  }

  public CExpression getCond() {
    return cond;
  }

  public CExpression getBranchTrue() {
    return branchTrue;
  }

  public CExpression getBranchFalse() {
    return branchFalse;
  }

}
