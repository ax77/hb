package ast_stmt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ast_main.GlobalCounter;
import utils_oth.NullChecker;

public class StmtBlock implements Serializable {
  private static final long serialVersionUID = -3746821242002590684L;

  private final List<StmtStatement> blockItems;
  private final int uniqueId;

  // empty: { }
  public StmtBlock() {
    this.blockItems = new ArrayList<>();
    this.uniqueId = GlobalCounter.next();
  }

  public void pushItemBack(StmtStatement item) {
    NullChecker.check(item);
    this.blockItems.add(item);
  }

  public void pushItemFront(StmtStatement item) {
    NullChecker.check(item);
    this.blockItems.add(0, item);
  }

  public List<StmtStatement> getBlockItems() {
    return blockItems;
  }

  public String getUniqueIdToString() {
    return String.format("%d", uniqueId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n{\n");
    for (StmtStatement blockItem : blockItems) {
      sb.append(blockItem.toString());
    }
    sb.append("\n}\n");
    return sb.toString();
  }

}
