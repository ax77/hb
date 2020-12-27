package njast.ast_class;

import java.util.ArrayList;
import java.util.List;

public class Block {
  //  <block> ::= { <block statements>? }
  //  <block statements> ::= <block statement> | <block statements> <block statement>

  private final List<BlockStatement> blockStatements;

  public Block() {
    this.blockStatements = new ArrayList<BlockStatement>();
  }

  public void put(BlockStatement e) {
    this.blockStatements.add(e);
  }

  public List<BlockStatement> getBlockStatements() {
    return blockStatements;
  }

}
