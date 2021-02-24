package ast_stmt;

import utils_oth.NullChecker;

public class StmtBreak {
  private final StmtFor loop;

  public StmtBreak(StmtFor loop) {
    NullChecker.check(loop);
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
