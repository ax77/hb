package ast_st3_tac.ir;

import ast_st3_tac.vars.store.Var;

public class StoreVarAssignOpCall {
  private final Var dst;
  private final AuxCallAssignOpArgVar opAssign;

  public StoreVarAssignOpCall(Var dst, AuxCallAssignOpArgVar opAssign) {
    this.dst = dst;
    this.opAssign = opAssign;
  }

}
