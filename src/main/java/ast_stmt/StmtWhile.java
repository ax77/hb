package ast_stmt;

import ast_expr.ExprExpression;

public class StmtWhile {
  private final ExprExpression condition;
  private final StmtBlock block;

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

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("while ");
    sb.append(condition.toString());
    sb.append(block.toString());
    return sb.toString();
  }

}
