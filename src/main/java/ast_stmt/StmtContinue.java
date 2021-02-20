package ast_stmt;

public class StmtContinue {
  private final StmtStatement loop;

  public StmtContinue(StmtStatement loop) {
    this.loop = loop;
  }

  public StmtStatement getLoop() {
    return loop;
  }

  @Override
  public String toString() {
    return "continue;";
  }

}
