package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.FunctionCallWithResult;
import _st3_linearize_expr.leaves.Var;

public class AssignVarFlatCallResultHashFn {
  private final Var lvalue;
  private final FunctionCallWithResult rvalue;

  public AssignVarFlatCallResultHashFn(Var lvalue, FunctionCallWithResult rvalue) {
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
