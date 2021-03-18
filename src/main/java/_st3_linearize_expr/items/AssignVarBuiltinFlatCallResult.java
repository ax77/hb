package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.FunctionCallWithResultBuiltin;
import _st3_linearize_expr.leaves.Var;

public class AssignVarBuiltinFlatCallResult {
  private final Var lvalue;
  private final FunctionCallWithResultBuiltin rvalue;

  public AssignVarBuiltinFlatCallResult(Var lvalue, FunctionCallWithResultBuiltin rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public FunctionCallWithResultBuiltin getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.toString();
  }
}
