package ast_st3_tac.items;

import ast_st3_tac.calls.FlatCallAssignOpArgVar;
import ast_st3_tac.leaves.Var;

public class StoreVarAssignOpCall {
  private final Var dst;
  private final FlatCallAssignOpArgVar opAssign;

  public StoreVarAssignOpCall(Var dst, FlatCallAssignOpArgVar opAssign) {
    this.dst = dst;
    this.opAssign = opAssign;
  }

  public Var getDst() {
    return dst;
  }

  public FlatCallAssignOpArgVar getOpAssign() {
    return opAssign;
  }

  @Override
  public String toString() {
    return dst.toString() + " = " + opAssign.toString();
  }

}
