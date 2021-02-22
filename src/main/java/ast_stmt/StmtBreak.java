package ast_stmt;

public class StmtBreak {
  private final StmtFor loop;

  public StmtBreak(StmtFor loop) {
    this.loop = loop;
  }

  public StmtFor getLoop() {
    return loop;
  }

  @Override
  public String toString() {
    return "break;";
  }

}
