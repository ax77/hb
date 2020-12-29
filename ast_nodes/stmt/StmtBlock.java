package njast.ast_nodes.stmt;

import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class StmtBlock implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }
  //  <block> ::= { <block statements>? }
  //  <block statements> ::= <block statement> | <block statements> <block statement>

  private final StmtBlockStatements blockStatements;

  public StmtBlock(StmtBlockStatements blockStatements) {
    this.blockStatements = blockStatements;
  }

  public StmtBlockStatements getBlockStatements() {
    return blockStatements;
  }

}
