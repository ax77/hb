package njast.ast_nodes.stmt;

import java.io.Serializable;

import njast.ast_nodes.expr.ExprExpression;

public class Stmt_if implements Serializable {
  private static final long serialVersionUID = 8138015838549729527L;

  private final ExprExpression ifexpr;
  private final StmtStatement ifstmt;
  private final StmtStatement ifelse;

  public Stmt_if(ExprExpression ifexpr, StmtStatement ifstmt, StmtStatement ifelse) {
    this.ifexpr = ifexpr;
    this.ifstmt = ifstmt;
    this.ifelse = ifelse;
  }

  public ExprExpression getIfexpr() {
    return ifexpr;
  }

  public StmtStatement getIfstmt() {
    return ifstmt;
  }

  public StmtStatement getIfelse() {
    return ifelse;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("if(");
    sb.append(ifexpr.toString());
    sb.append(")");

    sb.append("\n{\n");
    if (ifstmt != null) {
      sb.append(ifstmt.toString().trim());
    }
    sb.append("\n}\n");

    if (ifelse != null) {
      sb.append("else");
      sb.append("\n{\n");
      sb.append(ifelse.toString());
      sb.append("\n}\n");
    }

    return sb.toString();
  }

}
