package ast_st3_tac.items;

import ast_st3_tac.assign_ops.FieldVarAssignOp;
import ast_st3_tac.leaves.FieldAccess;

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
