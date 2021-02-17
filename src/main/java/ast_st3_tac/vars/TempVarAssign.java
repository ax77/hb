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
