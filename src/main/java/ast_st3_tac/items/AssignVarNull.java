package ast_st3_tac.items;

import ast_st3_tac.leaves.Var;

public class AssignVarNull {
  private final Var lvalue;
  private final String nullLiteral;

  public AssignVarNull(Var lvalue) {
    this.lvalue = lvalue;
    this.nullLiteral = "null";
  }

  public Var getLvalue() {
    return lvalue;
  }

  public String getNullLiteral() {
    return nullLiteral;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = null";
  }

}
