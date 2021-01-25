package ast_stmt;

import java.io.Serializable;

import ast_expr.ExprExpression;

public class Stmt_if implements Serializable {
  private static final long serialVersionUID = 8138015838549729527L;

  private final ExprExpression condition;
  private final StmtBlock trueStatement;
  private final StmtBlock optionalElseStatement;

  public Stmt_if(ExprExpression condition, StmtBlock trueStatement, StmtBlock optionalElseStatement) {
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

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("if(");
    sb.append(condition.toString());
    sb.append(")");

    sb.append("\n{\n");
    if (trueStatement != null) {
      sb.append(trueStatement.toString());
    }
    sb.append("\n}\n");

    if (optionalElseStatement != null) {
      sb.append("else");
      sb.append("\n{\n");
      sb.append(optionalElseStatement.toString());
      sb.append("\n}\n");
    }

    return sb.toString();
  }

}
