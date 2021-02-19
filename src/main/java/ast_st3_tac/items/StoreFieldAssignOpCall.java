package ast_st3_tac.items;

import ast_st3_tac.calls.FlatCallAssignOpArgField;
import ast_st3_tac.leaves.FieldAccess;

public class StoreFieldAssignOpCall {
  private final FieldAccess dst;
  private final FlatCallAssignOpArgField opAssign;

  public StoreFieldAssignOpCall(FieldAccess dst, FlatCallAssignOpArgField opAssign) {
    this.dst = dst;
    this.opAssign = opAssign;
  }

}
