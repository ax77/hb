package _st3_linearize_expr.items;

import _st3_linearize_expr.assign_ops.FieldVarAssignOp;
import _st3_linearize_expr.leaves.FieldAccess;

public class StoreFieldVarAssignOp {
  private final FieldAccess dst;
  private final FieldVarAssignOp opAssign;

  public StoreFieldVarAssignOp(FieldAccess dst, FieldVarAssignOp opAssign) {
    this.dst = dst;
    this.opAssign = opAssign;
  }

  public FieldAccess getDst() {
    return dst;
  }

  public FieldVarAssignOp getOpAssign() {
    return opAssign;
  }

  @Override
  public String toString() {
    return dst.toString() + " = " + opAssign.toString();
  }

}
