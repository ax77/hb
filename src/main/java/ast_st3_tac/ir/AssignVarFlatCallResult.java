package ast_st3_tac.ir;

import ast_st3_tac.vars.store.Var;

public class AssignVarFlatCallResult {
  private final Var lvalue;
  private final FlatCallResult rvalue;

  public AssignVarFlatCallResult(Var lvalue, FlatCallResult rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public FlatCallResult getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.toString();
  }
}
