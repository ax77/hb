package _st4_linearize_stmt.items;

public class LinearContinue {
  private final LinearLoop loop;

  public LinearContinue(LinearLoop loop) {
    this.loop = loop;
  }

  public LinearLoop getLoop() {
    return loop;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\ncontinue;");
    return sb.toString();
  }

}
