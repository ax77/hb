package ast_st3_tac.items;

import ast_st3_tac.leaves.PureFunctionCallWithResult;
import ast_st3_tac.leaves.Var;

public class AssignVarFlatCallClassCreationTmp {
  private final Var lvalue;
  private final PureFunctionCallWithResult rvalue;

  public AssignVarFlatCallClassCreationTmp(Var lvalue, PureFunctionCallWithResult rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public PureFunctionCallWithResult getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.toString();
  }
}
