package _st4_linearize_stmt.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.items.FlatCallVoid;

public class LocalDestructors {
  private final List<FlatCallVoid> calls;

  public LocalDestructors() {
    this.calls = new ArrayList<>();
  }

  public void add(FlatCallVoid e) {
    this.calls.add(e);
  }

  public List<FlatCallVoid> getCalls() {
    return calls;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (FlatCallVoid fc : calls) {
      sb.append(fc.toString() + ";\n");
    }
    return sb.toString();
  }

  public boolean isEmpty() {
    return calls.isEmpty();
  }

}
