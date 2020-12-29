package njast.ast_nodes.stmt;

import java.util.List;

import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class StmtBlock implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }
  //  <block> ::= { <block statements>? }
  //  <block statements> ::= <block statement> | <block statements> <block statement>

  private final List<StmtBlockItem> blockStatements;

  public StmtBlock(List<StmtBlockItem> blockStatements) {
    this.blockStatements = blockStatements;
  }

  public List<StmtBlockItem> getBlockStatements() {
    return blockStatements;
  }

}
