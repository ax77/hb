package _st4_linearize_stmt.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.items.AssignVarFlatCallResult;

public class LocalDestructors {
  private final List<AssignVarFlatCallResult> destructors;

  public LocalDestructors() {
    this.destructors = new ArrayList<>();
  }

  public void add(AssignVarFlatCallResult e) {
    this.destructors.add(e);
  }

  public List<AssignVarFlatCallResult> getDestructors() {
    return destructors;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (AssignVarFlatCallResult fc : destructors) {
      sb.append(fc.getLvalue().getName().toString() + " = " + fc.getRvalue().toString() + ";\n");
    }
    return sb.toString();
  }

  public boolean isEmpty() {
    return destructors.isEmpty();
  }

}
