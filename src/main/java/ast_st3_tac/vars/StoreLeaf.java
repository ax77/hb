package ast_st3_tac.vars;

import ast_st3_tac.vars.store.ELvalue;
import ast_st3_tac.vars.store.ERvalue;

public class StoreLeaf {
  private final ELvalue lvalue;
  private final ERvalue rvalue;

  public StoreLeaf(ELvalue lvalue, ERvalue rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public ELvalue getLvalue() {
    return lvalue;
  }

  public ERvalue getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.toString() + " = " + rvalue.toString();
  }

}
