package _st4_linearize_stmt.items;

import _st3_linearize_expr.leaves.Var;

public class LinearReturn {

  private final Var result;
  private BlockPrePost destructors;

  public LinearReturn(Var result) {
    this.result = result;
  }

  public boolean hasResult() {
    return result != null;
  }

  public BlockPrePost getDestructors() {
    return destructors;
  }

  public void setDestructors(BlockPrePost destructors) {
    this.destructors = destructors;
  }

  public Var getResult() {
    return result;
  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();
    if (destructors != null) {
      sb.append(destructors.toString());
    }
    sb.append("\nreturn " + (result != null ? result.toString() : "") + ";");
    return sb.toString();

  }

}
