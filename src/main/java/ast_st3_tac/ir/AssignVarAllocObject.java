package ast_st3_tac.ir;

import ast_st3_tac.vars.store.AllocObject;
import ast_st3_tac.vars.store.Var;

public class AssignVarAllocObject {
  private final Var lvalue;
  private final AllocObject rvalue;

  public AssignVarAllocObject(Var lvalue, AllocObject rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public AllocObject getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.toString() + " = " + rvalue.toString();
  }

}
