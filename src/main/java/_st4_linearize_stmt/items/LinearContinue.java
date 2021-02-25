package _st4_linearize_stmt.items;

public class LinearContinue {
  private final LinearLoop loop;
  private LocalDestructors destructors;

  public LinearContinue(LinearLoop loop) {
    this.loop = loop;
  }

  public LinearLoop getLoop() {
    return loop;
  }

  public LocalDestructors getDestructors() {
    return destructors;
  }

  public void setDestructors(LocalDestructors destructors) {
    this.destructors = destructors;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (destructors != null) {
      sb.append(destructors.toString());
    }
    sb.append("\ncontinue;");
    return sb.toString();
  }

}
