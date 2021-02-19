package ast_st3_tac.items;

import ast_st3_tac.leaves.FieldAccess;
import ast_st3_tac.leaves.Var;

public class StoreFieldVar {
  private final FieldAccess dst;
  private final Var src;

  public StoreFieldVar(FieldAccess dst, Var src) {
    this.dst = dst;
    this.src = src;
  }

  public FieldAccess getDst() {
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
