package ast_st3_tac.vars;

import ast_st3_tac.vars.store.ELvalue;
import ast_st3_tac.vars.store.Var;

public class StoreLeaf {
  private final ELvalue lvalue;
  private final Var rvalue;

  public StoreLeaf(ELvalue lvalue, Var rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public ELvalue getLvalue() {
    return lvalue;
  }

  public Var getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.toString() + " = " + rvalue.toString();
  }

}
