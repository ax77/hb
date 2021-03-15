package _st4_linearize_stmt.items;

public class LinearBreak {
  private final LinearLoop loop;
  private final AuxFunctions auxFunctions;

  public LinearBreak(LinearLoop loop) {
    this.loop = loop;
    this.auxFunctions = new AuxFunctions();
  }

  public AuxFunctions getAuxFunctions() {
    return auxFunctions;
  }

  public LinearLoop getLoop() {
    return loop;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (!auxFunctions.isEmpty()) {
      sb.append(auxFunctions.toString());
    }
    sb.append("\nbreak;");
    return sb.toString();
  }

}
