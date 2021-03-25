package _st4_linearize_stmt;

import java.util.ArrayList;
import java.util.List;

import _st4_linearize_stmt.items.LinearStatement;
import _st4_linearize_stmt.items.LocalDestructors;
import ast_stmt.StatementBase;

public class LinearBlock {
  private final List<LinearStatement> items;
  private LocalDestructors destructors;

  public LinearBlock() {
    this.items = new ArrayList<>();
  }

  public void pushItemBack(LinearStatement e) {
    this.items.add(e);
  }

  public List<LinearStatement> getItems() {
    return items;
  }

  public LocalDestructors getDestructors() {
    return destructors;
  }

  public void setDestructors(LocalDestructors destructors) {
    this.destructors = destructors;
  }

  public boolean theLastItemIsReturn() {
    if (items.isEmpty()) {
      return false;
    }
    LinearStatement last = items.get(items.size() - 1);
    if (last.getBase() == StatementBase.SBLOCK) {
      final boolean theLastItemIsReturn = last.getLinearBlock().theLastItemIsReturn();
      return theLastItemIsReturn;
    }
    return last.getBase() == StatementBase.SRETURN;
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

    /// block post-processing
    if (destructors != null) {
      if (!theLastItemIsReturn()) {
        sb.append(destructors.toString());
      }
    }

    sb.append("\n}\n");
    return sb.toString();
  }

}
