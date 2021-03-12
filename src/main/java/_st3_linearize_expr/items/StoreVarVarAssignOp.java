package _st3_linearize_expr.items;

import _st3_linearize_expr.assign_ops.VarVarAssignOp;
import _st3_linearize_expr.leaves.Var;

public class StoreVarVarAssignOp {
  private final Var dst;
  private final VarVarAssignOp opAssign;

  public StoreVarVarAssignOp(Var dst, VarVarAssignOp opAssign) {
    this.dst = dst;
    this.opAssign = opAssign;
  }

  public Var getDst() {
    return dst;
  }

  public VarVarAssignOp getOpAssign() {
    return opAssign;
  }

  @Override
  public String toString() {
    return opAssign.toString();
  }

}
