package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.Var;

public class AssignVarFlatCallStringCreationTmp {
  private final Var lvalue;
  private final String rvalue;

  public AssignVarFlatCallStringCreationTmp(Var lvalue, String rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public String getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.toString();
  }

}
