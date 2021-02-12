package ast_stmt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ast_vars.VarDeclarator;
import utils_oth.NullChecker;

public class StmtBlock implements Serializable {
  private static final long serialVersionUID = -3746821242002590684L;

  private final List<StmtBlockItem> blockStatements;

  //@REFCOUNT
  private final List<VarDeclarator> variables;

  // empty: { }
  public StmtBlock() {
    this.blockStatements = new ArrayList<>();
    this.variables = new ArrayList<>();
  }

  public StmtBlock(List<StmtBlockItem> blockStatements) {
    NullChecker.check(blockStatements);
    this.blockStatements = blockStatements;
    this.variables = new ArrayList<>();
  }

  public List<StmtBlockItem> getBlockStatements() {
    return blockStatements;
  }

  public void put(StmtBlockItem item) {
    this.blockStatements.add(item);
  }

  public List<VarDeclarator> getVariables() {
    return variables;
  }

  public void registerVariable(VarDeclarator var) {
    this.variables.add(var);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n{\n");
    for (StmtBlockItem blockItem : blockStatements) {
      sb.append(blockItem.toString());
    }
    sb.append("\n}\n");
    return sb.toString();
  }

}
