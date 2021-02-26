package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.Unop;
import _st3_linearize_expr.leaves.Var;

public class AssignVarUnop {
  private final Var lvalue;
  private final Unop rvalue;

  public AssignVarUnop(Var lvalue, Unop rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public Unop getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.toString();
  }

}
