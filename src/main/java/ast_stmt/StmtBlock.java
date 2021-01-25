package ast_stmt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import utils_oth.NullChecker;

public class StmtBlock implements Serializable {
  private static final long serialVersionUID = -3746821242002590684L;

  private final List<StmtBlockItem> blockStatements;

  // empty: { }
  public StmtBlock() {
    this.blockStatements = new ArrayList<StmtBlockItem>(0);
  }

  public StmtBlock(List<StmtBlockItem> blockStatements) {
    NullChecker.check(blockStatements);

    this.blockStatements = blockStatements;
  }

  public List<StmtBlockItem> getBlockStatements() {
    return blockStatements;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (StmtBlockItem blockItem : blockStatements) {
      sb.append(blockItem.toString());
    }
    return sb.toString();
  }

}
