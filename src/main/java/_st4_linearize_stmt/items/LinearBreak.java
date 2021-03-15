package _st4_linearize_stmt.items;

public class LinearBreak {
  private final LinearLoop loop;
  private BlockPrePost destructors;

  public LinearBreak(LinearLoop loop) {
    this.loop = loop;
  }

  public BlockPrePost getDestructors() {
    return destructors;
  }

  public void setDestructors(BlockPrePost destructors) {
    this.destructors = destructors;
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

}
