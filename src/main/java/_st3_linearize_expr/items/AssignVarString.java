package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.Var;

public class AssignVarString {
  private final Var lvalue;
  private final String literal;

  public AssignVarString(Var lvalue, String literal) {
    this.lvalue = lvalue;
    this.literal = literal;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public String getLiteral() {
    return literal;
  }

  @Override
  public String toString() {
    return lvalue.toString() + " = " + literal;
  }
}
