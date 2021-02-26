package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.Binop;
import _st3_linearize_expr.leaves.Var;

public class AssignVarBinop {
  private final Var lvalue;
  private final Binop rvalue;

  public AssignVarBinop(Var lvalue, Binop rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public Binop getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.toString();
  }

}
