package _st4_linearize_stmt.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.items.FlatCallVoid;

public class LocalDestructors {
  private final List<FlatCallVoid> destructors;

  public LocalDestructors() {
    this.destructors = new ArrayList<>();
  }

  public void add(FlatCallVoid e) {
    this.destructors.add(e);
  }

  public List<FlatCallVoid> getDestructors() {
    return destructors;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (FlatCallVoid fc : destructors) {
      sb.append(fc.toString() + ";\n");
    }
    return sb.toString();
  }

  public boolean isEmpty() {
    return destructors.isEmpty();
  }

}
