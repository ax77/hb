package ast_stmt;

import java.io.Serializable;

import ast_expr.ExprExpression;
import utils_oth.NullChecker;

public class StmtSelect implements Serializable {
  private static final long serialVersionUID = 8138015838549729527L;

  private final ExprExpression condition;
  private final StmtStatement trueStatement;
  private final StmtStatement optionalElseStatement;

  public StmtSelect(ExprExpression condition, StmtStatement trueStatement, StmtStatement optionalElseStatement) {
    NullChecker.check(condition, trueStatement);

    this.condition = condition;
    this.trueStatement = trueStatement;
    this.optionalElseStatement = optionalElseStatement;
  }

  public ExprExpression getCondition() {
    return condition;
  }

  public StmtStatement getTrueStatement() {
    return trueStatement;
  }

  public StmtStatement getOptionalElseStatement() {
    return optionalElseStatement;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("if ");
    sb.append(condition.toString());

    sb.append(trueStatement.toString());

    if (optionalElseStatement != null) {
      sb.append("else ");
      sb.append(optionalElseStatement.toString());
    }

    return sb.toString();
  }

}
