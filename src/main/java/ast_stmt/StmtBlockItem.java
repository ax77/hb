package ast_stmt;

import java.io.Serializable;

import ast_st3_tac.FlatCode;
import ast_vars.VarDeclarator;
import utils_oth.NullChecker;

public class StmtBlockItem implements Serializable {
  private static final long serialVersionUID = -5769795901942280395L;

  private VarDeclarator localVariable;
  private StmtStatement statement;

  /// 3ac
  private FlatCode linearLocalVariable;

  public StmtBlockItem(VarDeclarator localVariable) {
    NullChecker.check(localVariable);
    this.localVariable = localVariable;
  }

  public StmtBlockItem(StmtStatement statement) {
    NullChecker.check(statement);
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

  public boolean isVarDeclarationItem() {
    return localVariable != null;
  }

  public boolean isStatementItem() {
    return statement != null;
  }

  public FlatCode getLinearLocalVariable() {
    return linearLocalVariable;
  }

  public void setLinearLocalVariable(FlatCode linearLocalVariable) {
    this.linearLocalVariable = linearLocalVariable;
  }

  @Override
  public String toString() {
    if (localVariable != null) {
      String header = (linearLocalVariable == null ? "" : linearLocalVariable.toString());

      StringBuilder sb = new StringBuilder();
      sb.append(header);
      sb.append(localVariable.toString() + "\n");
      return sb.toString();
    }
    if (statement != null) {
      return statement.toString() + "\n";
    }
    return "??? StmtBlockItem-is empty";
  }

}
