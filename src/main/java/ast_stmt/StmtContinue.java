package ast_stmt;

public class StmtContinue {
  private final StmtFor loop;

  public StmtContinue(StmtFor loop) {
    this.loop = loop;
  }

  public StmtFor getLoop() {
    return loop;
  }

  @Override
  public String toString() {
    return "continue;";
  }

}
