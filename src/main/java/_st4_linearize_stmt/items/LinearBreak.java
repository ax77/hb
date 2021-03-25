package _st4_linearize_stmt.items;

public class LinearBreak {
  private final LinearLoop loop;
  private LocalDestructors destructors;

  public LinearBreak(LinearLoop loop) {
    this.loop = loop;
  }

  public LocalDestructors getDestructors() {
    return destructors;
  }

  public LinearLoop getLoop() {
    return loop;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (destructors != null) {
      sb.append(destructors.toString());
    }
    sb.append("\nbreak;");
    return sb.toString();
  }

  public void setDestructors(LocalDestructors destructors) {
    this.destructors = destructors;
  }

}
