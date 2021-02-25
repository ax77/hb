package ast_st5_stmts.execs;

public class LinearBreak {
  private final LinearLoop loop;
  private LocalDestructors destructors;

  public LinearBreak(LinearLoop loop) {
    this.loop = loop;
  }

  @Override
  public String toString() {
    return "break;";
  }

}
