package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.Var;

public class IntrinsicText {
  private final Var dest;
  private final String text;

  public IntrinsicText(Var dest, String text) {
    this.dest = dest;
    this.text = text;
  }

  public Var getDest() {
    return dest;
  }

  public String getText() {
    return text;
  }

  @Override
  public String toString() {
    return text;
  }
}
