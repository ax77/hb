package _st3_linearize_expr.items;

import _st3_linearize_expr.assign_ops.VarFieldAssignOp;
import _st3_linearize_expr.leaves.Var;

public class StoreVarFieldAssignOp {
  private final Var dst;
  private final VarFieldAssignOp opAssign;

  public StoreVarFieldAssignOp(Var dst, VarFieldAssignOp opAssign) {
    this.dst = dst;
    this.opAssign = opAssign;
  }

  public Var getDst() {
    return dst;
  }

  public VarFieldAssignOp getOpAssign() {
    return opAssign;
  }

  @Override
  public String toString() {
    return dst.toString() + " = " + opAssign.toString();
  }

}
