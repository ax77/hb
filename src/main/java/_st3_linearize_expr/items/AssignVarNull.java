package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.Var;

public class AssignVarNull {
  private final Var lvalue;
  private final String literal;

  public AssignVarNull(Var lvalue) {
    this.lvalue = lvalue;
    this.literal = "NULL";
  }

  public Var getLvalue() {
    return lvalue;
  }

  public String getLiteral() {
    return literal;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + literal;
  }

}
