package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.FieldAccess;
import _st3_linearize_expr.leaves.Var;

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
