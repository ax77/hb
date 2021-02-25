package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.Ternary;
import _st3_linearize_expr.leaves.Var;

public class AssignVarTernaryOp {
  private final Var lvalue;
  private final Ternary rvalue;

  public AssignVarTernaryOp(Var lvalue, Ternary rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public Ternary getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.toString();
  }

}
