package _st4_linearize_stmt;

import java.util.ArrayList;
import java.util.List;

import _st4_linearize_stmt.items.LinearStatement;

public class LinearBlock {
  private final List<LinearStatement> items;

  public LinearBlock() {
    this.items = new ArrayList<>();
  }

  public void pushItemBack(LinearStatement e) {
    this.items.add(e);
  }

  public List<LinearStatement> getItems() {
    return items;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n{\n");

    /// block body
    for (LinearStatement s : items) {
      sb.append(s.toString());
      sb.append("\n");
    }

    sb.append("\n}\n");
    return sb.toString();
  }

}
