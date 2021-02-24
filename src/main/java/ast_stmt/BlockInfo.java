package ast_stmt;

public class BlockInfo {
  private final StmtBlock methodBlock;
  private final StmtBlock blockOwner;
  private final boolean isMethodBlock;

  public BlockInfo(StmtBlock methodBlock, StmtBlock blockOwner) {
    this.methodBlock = methodBlock;
    this.blockOwner = blockOwner;
    this.isMethodBlock = false;
  }

  public BlockInfo() {
    this.methodBlock = null;
    this.blockOwner = null;
    this.isMethodBlock = true;
  }

  public StmtBlock getMethodBlock() {
    return methodBlock;
  }

  public StmtBlock getBlockOwner() {
    return blockOwner;
  }

  public boolean isMethodBlock() {
    return isMethodBlock;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (isMethodBlock) {
      return "root";
    }
    if (methodBlock != null) {
      sb.append("M:" + methodBlock.getUniqueIdToString() + " ");
    }
    if (blockOwner != null) {
      sb.append("O:" + blockOwner.getUniqueIdToString());
    }
    return sb.toString();
  }

}
