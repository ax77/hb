package ast_st3_tac.vars;

import ast_st3_tac.vars.store.Rvalue;
import ast_st3_tac.vars.store.Var;

public class TempVarAssign {
  private final Var var;
  private final Rvalue rvalue;

  public TempVarAssign(Var var, Rvalue rvalue) {
    this.var = var;
    this.rvalue = rvalue;
  }

  public Var getVar() {
    return var;
  }

  public Rvalue getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return var.toString() + " = " + rvalue.toString();
  }

}
