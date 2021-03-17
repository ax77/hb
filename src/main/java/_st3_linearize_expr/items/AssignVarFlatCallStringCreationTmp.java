package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.PureFunctionCallWithResult;
import _st3_linearize_expr.leaves.Var;

public class AssignVarFlatCallStringCreationTmp {
  private final Var lvalue;
  private final PureFunctionCallWithResult constructor;
  private final String rvalue;

  public AssignVarFlatCallStringCreationTmp(Var lvalue, String rvalue, PureFunctionCallWithResult constructor) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
    this.constructor = constructor;
  }

  public PureFunctionCallWithResult getConstructor() {
    return constructor;
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
