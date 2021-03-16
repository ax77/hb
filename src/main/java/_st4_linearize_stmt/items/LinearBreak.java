package _st4_linearize_stmt.items;

public class LinearBreak {
  private final LinearLoop loop;

  public LinearBreak(LinearLoop loop) {
    this.loop = loop;
  }

  public LinearLoop getLoop() {
    return loop;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\nbreak;");
    return sb.toString();
  }

}
