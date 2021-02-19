package ast_st3_tac.items;

import ast_st3_tac.leaves.Var;
import literals.IntLiteral;

public class AssignVarNum {
  private final Var lvalue;
  private final IntLiteral num;

  public AssignVarNum(Var lvalue, IntLiteral num) {
    this.lvalue = lvalue;
    this.num = num;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public IntLiteral getNum() {
    return num;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + num.toString();
  }

}
