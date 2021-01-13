package njast.ast_nodes.stmt;

import java.io.Serializable;

import njast.ast_nodes.clazz.vars.VarDeclarator;

public class StmtBlockItem implements Serializable {
  private static final long serialVersionUID = -5769795901942280395L;

  private VarDeclarator localVariable;
  private StmtStatement statement;

  public StmtBlockItem(VarDeclarator localVariable) {
    this.localVariable = localVariable;
  }

  public StmtBlockItem(StmtStatement statement) {
    this.statement = statement;
  }

  public VarDeclarator getLocalVariable() {
    return localVariable;
  }

  public StmtStatement getStatement() {
    return statement;
  }

  public void setStatement(StmtStatement statement) {
    this.statement = statement;
  }

  @Override
  public String toString() {
    if (localVariable != null) {
      StringBuilder sb = new StringBuilder();
      sb.append(localVariable.toString() + "\n");
      return sb.toString();
    }
    if (statement != null) {
      return statement.toString() + "\n";
    }
    return "STMT";
  }

}
