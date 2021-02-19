package ast_st3_tac.items;

import ast_st3_tac.assign_ops.VarFieldAssignOp;
import ast_st3_tac.leaves.Var;

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
