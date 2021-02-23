package ast_stmt;

public class StmtContinue {
  private final StmtFor loop;
  private final StmtBlock closestBlock;

  public StmtContinue(StmtFor loop, StmtBlock closestBlock) {
    this.loop = loop;
    this.closestBlock = closestBlock;
  }

  public StmtFor getLoop() {
    return loop;
  }

  public StmtBlock getClosestBlock() {
    return closestBlock;
  }

  @Override
  public String toString() {
    return "continue;";
  }

}
