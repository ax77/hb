package ast_st3_tac;

import java.util.ArrayList;
import java.util.List;

import ast_st3_tac.ir.FlatCodeItem;
import ast_st3_tac.leaves.Var;

public class FlatCode {
  private final List<FlatCodeItem> items;

  public FlatCode() {
    this.items = new ArrayList<>();
  }

  public FlatCode(List<FlatCodeItem> items) {
    this.items = items;
  }

  public FlatCodeItem getLast() {
    return items.get(items.size() - 1);
  }

  public Var getDest() {
    return getLast().getDest();
  }

  public List<FlatCodeItem> getItems() {
    return items;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (FlatCodeItem item : items) {
      sb.append("// " + item.toString() + "\n");
    }
    return sb.toString();
  }

}
