package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.FunctionCallWithResult;
import _st3_linearize_expr.leaves.Var;

public class AssignVarFlatCallResult {
  private final Var lvalue;
  private final FunctionCallWithResult rvalue;

  public AssignVarFlatCallResult(Var lvalue, FunctionCallWithResult rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public FunctionCallWithResult getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.toString();
  }
}
