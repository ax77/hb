package _st4_linearize_stmt;

import java.util.ArrayList;
import java.util.List;

import _st4_linearize_stmt.items.LinearStatement;
import _st4_linearize_stmt.items.BlockPrePost;
import ast_stmt.StatementBase;

public class LinearBlock {
  private final List<LinearStatement> items;
  private BlockPrePost onEnter;
  private BlockPrePost onExit;

  public LinearBlock() {
    this.items = new ArrayList<>();
    this.onEnter = new BlockPrePost();
    this.onExit = new BlockPrePost();
  }

  public void pushItemBack(LinearStatement e) {
    this.items.add(e);
  }

  public List<LinearStatement> getItems() {
    return items;
  }

  public void setOnExit(BlockPrePost prepost) {
    this.onExit = prepost;
  }

  public BlockPrePost getOnExit() {
    return onExit;
  }

  public BlockPrePost getOnEnter() {
    return onEnter;
  }

  public void setOnEnter(BlockPrePost onEnter) {
    this.onEnter = onEnter;
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

    /// block pre-processing
    if (onEnter != null && !onEnter.isEmpty()) {
      sb.append(onEnter.toString());
    }

    /// block body
    for (LinearStatement s : items) {
      sb.append(s.toString());
      sb.append("\n");
    }

    /// block post-processing
    if (onExit != null) {
      if (!onExit.isEmpty()) {
        if (!theLastItemIsReturn()) {
          sb.append(onExit.toString());
        }
      }
    }

    sb.append("\n}\n");
    return sb.toString();
  }

}
