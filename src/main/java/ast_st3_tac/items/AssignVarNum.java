package ast_st3_tac.items;

import ast_st3_tac.leaves.Var;
import literals.IntLiteral;

public class AssignVarNum {
  private final Var lvalue;
  private final IntLiteral literal;

  public AssignVarNum(Var lvalue, IntLiteral literal) {
    this.lvalue = lvalue;
    this.literal = literal;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public IntLiteral getLiteral() {
    return literal;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + literal.toString();
  }

}
