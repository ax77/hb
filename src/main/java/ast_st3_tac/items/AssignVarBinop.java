package ast_st3_tac.items;

import ast_st3_tac.leaves.Binop;
import ast_st3_tac.leaves.Var;

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
