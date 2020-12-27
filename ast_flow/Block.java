package njast.ast_flow;

import java.util.ArrayList;
import java.util.List;

public class Block {
  //  <block> ::= { <block statements>? }
  //  <block statements> ::= <block statement> | <block statements> <block statement>

  private final List<BlockStatement> blockStatements;

  public Block() {
    this.blockStatements = new ArrayList<BlockStatement>();
  }

  public Block(List<BlockStatement> blockStatements) {
    this.blockStatements = blockStatements;
  }

  public List<BlockStatement> getBlockStatements() {
    return blockStatements;
  }

}
