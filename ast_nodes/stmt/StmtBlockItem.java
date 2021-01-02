package njast.ast_nodes.stmt;

import java.io.Serializable;
import java.util.List;

import njast.ast_nodes.clazz.vars.VarDeclarator;

public class StmtBlockItem implements Serializable {
  private static final long serialVersionUID = -5769795901942280395L;
  
  private List<VarDeclarator> localVars;
  private StmtStatement statement;

  public StmtBlockItem(List<VarDeclarator> localVars) {
    this.localVars = localVars;
  }

  public StmtBlockItem(StmtStatement statement) {
    this.statement = statement;
  }

  public List<VarDeclarator> getLocalVars() {
    return localVars;
  }

  public void setLocalVars(List<VarDeclarator> localVars) {
    this.localVars = localVars;
  }

  public StmtStatement getStatement() {
    return statement;
  }

  public void setStatement(StmtStatement statement) {
    this.statement = statement;
  }

  @Override
  public String toString() {
    if (localVars != null) {
      return "LOC_VAR_DECL";
    }
    return "STMT";
  }

}
