package ast_stmt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ast_st3_tac.FlatCode;
import utils_oth.NullChecker;

public class StmtBlock implements Serializable {
  private static final long serialVersionUID = -3746821242002590684L;

  private final List<StmtBlockItem> blockItems;

  /// 3ac
  private final List<StmtReturn> returns;
  private FlatCode destr;

  // empty: { }
  public StmtBlock() {
    this.blockItems = new ArrayList<>();
    this.returns = new ArrayList<>();
    this.destr = new FlatCode();
  }

  public StmtBlock(List<StmtBlockItem> blockStatements) {
    NullChecker.check(blockStatements);
    this.blockItems = blockStatements;
    this.returns = new ArrayList<>();
    this.destr = new FlatCode();
  }

  public void pushItemBack(StmtBlockItem item) {
    NullChecker.check(item);
    this.blockItems.add(item);
  }

  public void pushItemFront(StmtBlockItem item) {
    NullChecker.check(item);
    this.blockItems.add(0, item);
  }

  public List<StmtBlockItem> getBlockItems() {
    return blockItems;
  }

  public void addReturn(StmtReturn e) {
    returns.add(e);
  }

  public List<StmtReturn> getReturns() {
    return returns;
  }

  public FlatCode getDestr() {
    return destr;
  }

  public void setDestr(FlatCode destr) {
    this.destr = destr;
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
