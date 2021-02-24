package ast_stmt;

import utils_oth.NullChecker;

public class StmtContinue {
  private final StmtFor loop;

  public StmtContinue(StmtFor loop) {
    NullChecker.check(loop);
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
