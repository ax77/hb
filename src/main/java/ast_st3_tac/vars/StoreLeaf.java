package ast_st3_tac.vars;

import ast_st3_tac.vars.store.Lvalue;
import ast_st3_tac.vars.store.Rvalue;

public class StoreLeaf {
  private final Lvalue lvalue;
  private final Rvalue rvalue;

  public StoreLeaf(Lvalue lvalue, Rvalue rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Lvalue getLvalue() {
    return lvalue;
  }

  public Rvalue getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.toString() + " = " + rvalue.toString();
  }

}
