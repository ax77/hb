package ast_st3_tac.items;

import ast_st3_tac.calls.FlatCallAssignOpArgArray;
import ast_st3_tac.leaves.ArrayAccess;

public class StoreArrayAssignOpCall {
  private final ArrayAccess dst;
  private final FlatCallAssignOpArgArray opAssign;

  public StoreArrayAssignOpCall(ArrayAccess dst, FlatCallAssignOpArgArray opAssign) {
    this.dst = dst;
    this.opAssign = opAssign;
  }

  public ArrayAccess getDst() {
    return dst;
  }

  public FlatCallAssignOpArgArray getOpAssign() {
    return opAssign;
  }

  @Override
  public String toString() {
    return dst.toString() + " = " + opAssign.toString();
  }
}
