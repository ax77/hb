package ast_st5_stmts.execs;

public class LinearBreak {
  private final LinearLoop loop;
  private LocalDestructors destructors;

  public LinearBreak(LinearLoop loop) {
    this.loop = loop;
  }

  public LocalDestructors getDestructors() {
    return destructors;
  }

  public void setDestructors(LocalDestructors destructors) {
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