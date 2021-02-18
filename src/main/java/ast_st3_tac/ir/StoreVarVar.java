package ast_st3_tac.ir;

import ast_st3_tac.vars.store.Var;

public class StoreVarVar {
  private final Var dst;
  private final Var src;

  public StoreVarVar(Var dst, Var src) {
    this.dst = dst;
    this.src = src;
  }

  public Var getDst() {
    return dst;
  }

  public Var getSrc() {
    return src;
  }

  @Override
  public String toString() {
    return dst.toString() + " = " + src.toString();
  }

}
