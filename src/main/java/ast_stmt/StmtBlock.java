package ast_stmt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ast_printers.VarPrinters;
import ast_vars.VarDeclarator;
import utils_oth.NullChecker;

public class StmtBlock implements Serializable {
  private static final long serialVersionUID = -3746821242002590684L;

  private final List<StmtBlockItem> blockStatements;

  //@REFCOUNT
  private final List<VarDeclarator> variables;
  private final List<StmtReturn> returns;

  // empty: { }
  public StmtBlock() {
    this.blockStatements = new ArrayList<>();
    this.variables = new ArrayList<>();
    this.returns = new ArrayList<>();
  }

  public StmtBlock(List<StmtBlockItem> blockStatements) {
    NullChecker.check(blockStatements);
    this.blockStatements = blockStatements;
    this.variables = new ArrayList<>();
    this.returns = new ArrayList<>();
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

  public List<StmtReturn> getReturns() {
    return returns;
  }

  public void registerVariable(VarDeclarator var) {
    this.variables.add(var);
  }

  public void registerReturn(StmtReturn ret) {
    this.returns.add(ret);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n{\n");
    for (StmtBlockItem blockItem : blockStatements) {
      sb.append(blockItem.toString());
    }
    sb.append("\n}");
    sb.append(VarPrinters.bindedVarsComment(variables));
    sb.append("\n");
    return sb.toString();
  }

}
