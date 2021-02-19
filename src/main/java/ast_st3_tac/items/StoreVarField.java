package ast_st3_tac.items;

import ast_st3_tac.leaves.FieldAccess;
import ast_st3_tac.leaves.Var;

public class StoreVarField {
  private final Var dst;
  private final FieldAccess src;

  public StoreVarField(Var dst, FieldAccess src) {
    this.dst = dst;
    this.src = src;
  }

  public Var getDst() {
    return dst;
  }

  public FieldAccess getSrc() {
    return src;
  }

  @Override
  public String toString() {
    return dst.toString() + " = " + src.toString();
  }

}
