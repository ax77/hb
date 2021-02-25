package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.FieldAccess;
import _st3_linearize_expr.leaves.Var;

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
