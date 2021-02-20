package ast_stmt;

import java.io.Serializable;

import ast_expr.ExprExpression;
import ast_st3_tac.FlatCode;
import utils_oth.NullChecker;

public class StmtSelect implements Serializable {
  private static final long serialVersionUID = 8138015838549729527L;

  /// int a = 0;
  /// if (a == 0) {
  ///   a = 1;
  /// } else if (a == 1) {
  ///   a = 2;
  /// } else if (a == 2) {
  ///   a = 3;
  /// } else {
  ///   a = 32;
  /// }
  /// ::
  /// ::
  /// int a = 0;
  /// if (a == 0) {
  ///   a = 1;
  /// } else {
  ///   if (a == 1) {
  ///     a = 2;
  ///   } else {
  ///     if (a == 3) {
  ///       a = 3;
  ///     } else {
  ///       a = 32;
  ///     }
  ///   }
  /// }

  private final ExprExpression condition;
  private final StmtBlock trueStatement;
  private final StmtBlock optionalElseStatement;

  /// 3ac
  private FlatCode linearCondition;

  public StmtSelect(ExprExpression condition, StmtBlock trueStatement, StmtBlock optionalElseStatement) {
    NullChecker.check(condition, trueStatement);

    this.condition = condition;
    this.trueStatement = trueStatement;
    this.optionalElseStatement = optionalElseStatement;
  }

  public ExprExpression getCondition() {
    return condition;
  }

  public StmtBlock getTrueStatement() {
    return trueStatement;
  }

  public StmtBlock getOptionalElseStatement() {
    return optionalElseStatement;
  }

  public boolean hasElse() {
    return optionalElseStatement != null;
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
    sb.append("if(");
    sb.append(condition.toString());
    sb.append(")");

    sb.append(trueStatement.toString());

    if (optionalElseStatement != null) {
      sb.append("else ");
      sb.append(optionalElseStatement.toString());
    }

    return sb.toString();
  }

}
