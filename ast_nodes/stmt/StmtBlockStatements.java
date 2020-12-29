package njast.ast_nodes.stmt;

import java.util.ArrayList;
import java.util.List;

import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class StmtBlockStatements implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

  private final List<StmtBlockStatement> blockStatements;

  public StmtBlockStatements() {
    this.blockStatements = new ArrayList<StmtBlockStatement>();
  }

  public StmtBlockStatements(List<StmtBlockStatement> blockStatements) {
    this.blockStatements = blockStatements;
  }

  public List<StmtBlockStatement> getBlockStatements() {
    return blockStatements;
  }
}
