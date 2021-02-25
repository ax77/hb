package ast_st5_stmts.execs;

import java.util.ArrayList;
import java.util.List;

public class LinearBlock {
  private final List<LinearStatement> items;
  private final LocalDestructors destructors;

  public LinearBlock() {
    this.items = new ArrayList<>();
    this.destructors = new LocalDestructors();
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

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n{\n");
    for (LinearStatement s : items) {
      sb.append(s.toString());
      sb.append("\n");
    }
    sb.append("\n}\n");
    return sb.toString();
  }

}
