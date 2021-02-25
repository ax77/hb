package _st4_linearize_stmt.items;

import _st3_linearize_expr.leaves.Var;

public class LinearReturn {

  private final Var result;
  private LocalDestructors destructors;

  public LinearReturn(Var result) {
    this.result = result;
  }

  public boolean hasResult() {
    return result != null;
  }

  public LocalDestructors getDestructors() {
    return destructors;
  }

  public void setDestructors(LocalDestructors destructors) {
    this.destructors = destructors;
  }

  public Var getResult() {
    return result;
  }

  @Override
  public String toString() {
    return "return " + (result != null ? result.toString() : "") + ";";
  }

}
