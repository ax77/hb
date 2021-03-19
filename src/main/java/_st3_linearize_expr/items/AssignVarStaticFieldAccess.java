package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.FieldAccess;
import _st3_linearize_expr.leaves.Var;

public class AssignVarStaticFieldAccess {
  private final Var lvalue;
  private final FieldAccess rvalue;

  public AssignVarStaticFieldAccess(Var lvalue, FieldAccess rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public FieldAccess getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.getObject().getName() + "_" + rvalue.getField().getName();
  }
}
