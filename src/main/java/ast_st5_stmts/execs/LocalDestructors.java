package ast_st5_stmts.execs;

import java.util.ArrayList;
import java.util.List;

import ast_st3_tac.items.FlatCallVoid;

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

}
