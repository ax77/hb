package ast_st3_tac.ir;

import ast_st3_tac.vars.store.FieldAccess;

public class StoreFieldAssignOpCall {
  private final FieldAccess dst;
  private final AuxCallAssignOpArgField opAssign;

  public StoreFieldAssignOpCall(FieldAccess dst, AuxCallAssignOpArgField opAssign) {
    this.dst = dst;
    this.opAssign = opAssign;
  }

}
