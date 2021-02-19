package ast_st3_tac.items;

import ast_st3_tac.leaves.Var;

public class AssignVarNull {
  private final Var lvalue;
  private final String literal;

  public AssignVarNull(Var lvalue) {
    this.lvalue = lvalue;
    this.literal = "null";
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
