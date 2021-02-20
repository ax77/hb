package ast_stmt;

public class StmtBreak {
  private final StmtStatement loop;

  public StmtBreak(StmtStatement loop) {
    this.loop = loop;
  }

  public StmtStatement getLoop() {
    return loop;
  }

  @Override
  public String toString() {
    return "break;";
  }

}
