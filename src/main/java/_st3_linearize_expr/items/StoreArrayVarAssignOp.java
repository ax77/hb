package _st3_linearize_expr.items;

import _st3_linearize_expr.assign_ops.ArrayVarAssignOp;
import _st3_linearize_expr.leaves.ArrayAccess;

public class StoreArrayVarAssignOp {
  private final ArrayAccess dst;
  private final ArrayVarAssignOp opAssign;

  public StoreArrayVarAssignOp(ArrayAccess dst, ArrayVarAssignOp opAssign) {
    this.dst = dst;
    this.opAssign = opAssign;
  }

  public ArrayAccess getDst() {
    return dst;
  }

  public ArrayVarAssignOp getOpAssign() {
    return opAssign;
  }

  @Override
  public String toString() {
    return dst.toString() + " = " + opAssign.toString();
  }
}
