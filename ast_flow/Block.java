package njast.ast_flow;

public class Block {
  //  <block> ::= { <block statements>? }
  //  <block statements> ::= <block statement> | <block statements> <block statement>

  private final BlockStatements blockStatements;

  public Block(BlockStatements blockStatements) {
    this.blockStatements = blockStatements;
  }

  public BlockStatements getBlockStatements() {
    return blockStatements;
  }

}
