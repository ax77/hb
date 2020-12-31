package njast.ast_nodes.stmt;

import jscan.tokenize.Token;
import njast.ast_kinds.StatementBase;
import njast.ast_nodes.expr.ExprExpression;

public class StmtStatement {

  private final StatementBase base;
  private StmtBlock compound;
  private StmtReturn sreturn;
  private ExprExpression sexpression;
  private StmtFor sfor;

  public StmtStatement(StmtFor sfor) {
    this.base = StatementBase.SFOR;
    this.sfor = sfor;
  }

  public StmtStatement(ExprExpression sexpression) {
    this.base = StatementBase.SEXPR;
    this.sexpression = sexpression;
  }

  public StmtStatement(StmtReturn sreturn) {
    this.base = StatementBase.SRETURN;
    this.sreturn = sreturn;
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

  public StmtReturn getSreturn() {
    return sreturn;
  }

  public ExprExpression getSexpression() {
    return sexpression;
  }

  public StmtFor getSfor() {
    return sfor;
  }

  @Override
  public String toString() {
    return base.toString();
  }

}
