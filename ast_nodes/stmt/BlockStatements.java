package njast.ast_nodes.stmt;

import java.util.ArrayList;
import java.util.List;

public class BlockStatements {
  private final List<BlockStatement> blockStatements;

  public BlockStatements() {
    this.blockStatements = new ArrayList<BlockStatement>();
  }

  public BlockStatements(List<BlockStatement> blockStatements) {
    this.blockStatements = blockStatements;
  }

  public List<BlockStatement> getBlockStatements() {
    return blockStatements;
  }
}
