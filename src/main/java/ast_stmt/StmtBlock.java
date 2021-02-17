package ast_stmt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import utils_oth.NullChecker;

public class StmtBlock implements Serializable {
  private static final long serialVersionUID = -3746821242002590684L;

  private final List<StmtBlockItem> blockItems;

  // empty: { }
  public StmtBlock() {
    this.blockItems = new ArrayList<>();
  }

  public StmtBlock(List<StmtBlockItem> blockStatements) {
    NullChecker.check(blockStatements);
    this.blockItems = blockStatements;
  }

  public List<StmtBlockItem> getBlockItems() {
    return blockItems;
  }

  public void put(StmtBlockItem item) {
    this.blockItems.add(item);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n{\n");
    for (StmtBlockItem blockItem : blockItems) {
      sb.append(blockItem.toString());
    }
    sb.append("\n}\n");
    return sb.toString();
  }

}
