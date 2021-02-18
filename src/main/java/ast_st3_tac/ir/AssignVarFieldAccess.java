package ast_st3_tac.ir;

import ast_st3_tac.vars.store.FieldAccess;
import ast_st3_tac.vars.store.Var;

public class AssignVarFieldAccess {
  private final Var lvalue;
  private final FieldAccess rvalue;

  public AssignVarFieldAccess(Var lvalue, FieldAccess rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public FieldAccess getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.toString();
  }
}
