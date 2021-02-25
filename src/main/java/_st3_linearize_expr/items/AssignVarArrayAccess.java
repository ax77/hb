package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.ArrayAccess;
import _st3_linearize_expr.leaves.Var;

public class AssignVarArrayAccess {
  private final Var lvalue;
  private final ArrayAccess rvalue;

  public AssignVarArrayAccess(Var lvalue, ArrayAccess rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public ArrayAccess getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.toString();
  }

}
