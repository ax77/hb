package njast.ast_nodes.stmt;

import jscan.tokenize.Token;
import njast.ast_kinds.StatementBase;
import njast.ast_nodes.expr.ExprExpression;

public class StmtStatement {

  private final StatementBase base;
  private StmtBlock compound;
  private ExprExpression expr; // return expr or expr-stmt
  private StmtFor sfor;

  public StmtStatement(StmtFor sfor) {
    this.base = StatementBase.SFOR;
    this.sfor = sfor;
  }

  // return <expr> ;
  // <expr> 
  public StmtStatement(StatementBase base, ExprExpression expr) {
    this.base = base;
    this.expr = expr;
  }

  // {  }
  public StmtStatement(Token from, StmtBlock compound) {
    this.base = StatementBase.SBLOCK;
    this.compound = compound;
  }

  public StmtBlock getCompound() {
    return compound;
  }

  public void setCompound(StmtBlock compound) {
    this.compound = compound;
  }

  public StatementBase getBase() {
    return base;
  }

  public ExprExpression getSexpression() {
    return expr;
  }

  public StmtFor getSfor() {
    return sfor;
  }

  @Override
  public String toString() {
    return base.toString();
  }

}
