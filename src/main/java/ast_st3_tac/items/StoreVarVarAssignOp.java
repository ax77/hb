package ast_st3_tac.items;

import ast_st3_tac.assign_ops.VarVarAssignOp;
import ast_st3_tac.leaves.Var;

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
    return dst.toString() + " = " + opAssign.toString();
  }

}
