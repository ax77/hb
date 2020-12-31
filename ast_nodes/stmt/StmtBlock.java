package njast.ast_nodes.stmt;

import java.util.ArrayList;
import java.util.List;

import njast.parse.NullChecker;

public class StmtBlock {

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

}
