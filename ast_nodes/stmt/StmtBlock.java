package njast.ast_nodes.stmt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import njast.parse.NullChecker;

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

}
