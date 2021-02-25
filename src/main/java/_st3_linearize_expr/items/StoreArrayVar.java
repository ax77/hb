package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.ArrayAccess;
import _st3_linearize_expr.leaves.Var;

public class StoreArrayVar {
  private final ArrayAccess dst;
  private final Var src;

  public StoreArrayVar(ArrayAccess dst, Var src) {
    this.dst = dst;
    this.src = src;
  }

  public ArrayAccess getDst() {
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
