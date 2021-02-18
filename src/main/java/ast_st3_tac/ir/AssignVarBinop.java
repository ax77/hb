package ast_st3_tac.ir;

import ast_st3_tac.vars.arith.Binop;
import ast_st3_tac.vars.store.Var;

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
