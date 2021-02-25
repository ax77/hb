package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.Binop;
import _st3_linearize_expr.leaves.Var;

public class AssignVarBinop {
  private final Var lvalue;
  private final Binop binop;

  public AssignVarBinop(Var lvalue, Binop binop) {
    this.lvalue = lvalue;
    this.binop = binop;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public Binop getBinop() {
    return binop;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + binop.toString();
  }

}
