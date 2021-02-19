package ast_st3_tac.items;

import ast_st3_tac.assign_ops.ArrayVarAssignOp;
import ast_st3_tac.leaves.ArrayAccess;

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
