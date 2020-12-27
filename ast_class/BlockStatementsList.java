package njast.ast_class;

import java.util.ArrayList;
import java.util.List;

public class BlockStatementsList {
  private final List<BlockStatement> blockStatements;

  public BlockStatementsList() {
    this.blockStatements = new ArrayList<BlockStatement>();
  }

  public void put(BlockStatement e) {
    this.blockStatements.add(e);
  }

  public List<BlockStatement> getBlockStatements() {
    return blockStatements;
  }
}
