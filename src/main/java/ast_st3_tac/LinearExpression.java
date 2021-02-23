package ast_st3_tac;

import java.util.ArrayList;
import java.util.List;

import ast_st3_tac.ir.FlatCodeItem;
import ast_st3_tac.leaves.Var;

public class LinearExpression {
  private final List<FlatCodeItem> items;
  private final List<Var> allVars;

  public LinearExpression() {
    this.items = new ArrayList<>();
    this.allVars = new ArrayList<>();
  }

  public LinearExpression(List<FlatCodeItem> items) {
    this.items = items;
    this.allVars = new ArrayList<>();
  }

  public LinearExpression(List<FlatCodeItem> items, List<Var> allVars) {
    this.items = items;
    this.allVars = allVars;
  }

  public FlatCodeItem getLast() {
    return items.get(items.size() - 1);
  }

  public boolean isEmpty() {
    return items.isEmpty();
  }

  public Var getDest() {
    return getLast().getDest();
  }

  public String getDestToString() {
    Var res = getLast().getDest();
    return res.getName().getName();
  }

  public List<FlatCodeItem> getItems() {
    return items;
  }

  public List<Var> getAllVars() {
    return allVars;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (FlatCodeItem item : items) {
      sb.append(item.toString() + ";\n");
    }
    return sb.toString();
  }

}
