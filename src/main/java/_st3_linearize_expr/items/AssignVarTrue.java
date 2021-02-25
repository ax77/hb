package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.Var;

public class AssignVarTrue {
  private final Var lvalue;
  private final String literal;

  public AssignVarTrue(Var lvalue) {
    this.lvalue = lvalue;
    this.literal = "true";
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
