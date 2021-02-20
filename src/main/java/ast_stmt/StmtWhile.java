package ast_stmt;

import ast_expr.ExprExpression;
import ast_st3_tac.FlatCode;

public class StmtWhile {
  private final ExprExpression condition;
  private final StmtBlock block;

  /// 3ac
  private FlatCode linearCondition;

  public StmtWhile(ExprExpression condition, StmtBlock block) {
    this.condition = condition;
    this.block = block;
  }

  public ExprExpression getCondition() {
    return condition;
  }

  public StmtBlock getBlock() {
    return block;
  }

  public FlatCode getLinearCondition() {
    return linearCondition;
  }

  public void setLinearCondition(FlatCode linearCondition) {
    this.linearCondition = linearCondition;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("while ");
    sb.append(condition.toString());
    sb.append(block.toString());
    return sb.toString();
  }

}
