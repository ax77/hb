package ast_stmt;

import java.io.Serializable;

import ast_vars.VarDeclarator;
import utils_oth.NullChecker;

public class StmtBlockItem implements Serializable {
  private static final long serialVersionUID = -5769795901942280395L;

  private VarDeclarator localVariable;
  private StmtStatement statement;
  private final boolean isVarDeclaration;

  public StmtBlockItem(VarDeclarator localVariable) {
    NullChecker.check(localVariable);
    this.localVariable = localVariable;
    this.isVarDeclaration = true;
  }

  public StmtBlockItem(StmtStatement statement) {
    NullChecker.check(statement);
    this.statement = statement;
    this.isVarDeclaration = false;
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

  public boolean isVarDeclarationItem() {
    return isVarDeclaration;
  }

  public boolean isStatementItem() {
    return !isVarDeclaration;
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
    return "??? StmtBlockItem-is empty";
  }

}
