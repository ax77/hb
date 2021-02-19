package ast_st3_tac.items;

import ast_st3_tac.leaves.Unop;
import ast_st3_tac.leaves.Var;

public class AssignVarUnop {
  private final Var lvalue;
  private final Unop unop;

  public AssignVarUnop(Var lvalue, Unop unop) {
    this.lvalue = lvalue;
    this.unop = unop;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public Unop getUnop() {
    return unop;
  }
  
  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + unop.toString();
  }

}
