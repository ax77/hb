package ast_st3_tac.vars;

import ast_st3_tac.vars.store.ERvalue;
import ast_st3_tac.vars.store.Var;

public class TempVarAssign {
  private final Var var;
  private final ERvalue rvalue;

  public TempVarAssign(Var var, ERvalue rvalue) {
    this.var = var;
    this.rvalue = rvalue;
  }

  public Var getVar() {
    return var;
  }

  public ERvalue getRvalue() {
    return rvalue;
  }

  public String varToString() {
    StringBuilder sb = new StringBuilder();
    if (!var.getMods().isEmpty()) {
      sb.append(var.getMods().toString());
      sb.append(" ");
    }
    sb.append(var.getType().toString());
    sb.append(" ");
    sb.append(var.getName().toString());

    return sb.toString();
  }

  @Override
  public String toString() {
    return varToString() + " = " + rvalue.toString();
  }

}
