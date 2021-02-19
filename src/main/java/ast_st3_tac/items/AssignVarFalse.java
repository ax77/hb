package ast_st3_tac.items;

import ast_st3_tac.leaves.Var;

public class AssignVarFalse {
  private final Var lvalue;
  private final String literal;

  public AssignVarFalse(Var lvalue) {
    this.lvalue = lvalue;
    this.literal = "false";
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
