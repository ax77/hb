package ast_st3_tac.ir;

import ast_st3_tac.vars.store.Var;

public class AssignVarFlatCallClassCreationTmp {
  private final Var lvalue;
  private final FlatCallClassCreationTmp rvalue;

  public AssignVarFlatCallClassCreationTmp(Var lvalue, FlatCallClassCreationTmp rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public FlatCallClassCreationTmp getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.toString();
  }
}
