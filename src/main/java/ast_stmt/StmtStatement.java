package ast_stmt;

import java.io.Serializable;

import ast_expr.ExprExpression;
import tokenize.Token;

public class StmtStatement implements Serializable {
  private static final long serialVersionUID = 2946438995245230886L;

  private final StatementBase base;
  private StmtBlock bloskStmt;
  private ExprExpression exprStmt; // return expr or expr-stmt
  private StmtFor forStmt;
  private Stmt_if ifStmt;

  public StmtStatement(StmtFor forStmt) {
    this.base = StatementBase.SFOR;
    this.forStmt = forStmt;
  }

  public StmtStatement(Stmt_if ifStmt) {
    this.base = StatementBase.SIF;
    this.ifStmt = ifStmt;
  }

  // return <expr> ;
  // <expr> 
  public StmtStatement(StatementBase base, ExprExpression exprStmt) {
    this.base = base;
    this.exprStmt = exprStmt;
  }

  // {  }
  public StmtStatement(Token from, StmtBlock bloskStmt) {
    this.base = StatementBase.SBLOCK;
    this.bloskStmt = bloskStmt;
  }

  public StmtBlock getBlockStmt() {
    return bloskStmt;
  }

  public StatementBase getBase() {
    return base;
  }

  public ExprExpression getExprStmt() {
    return exprStmt;
  }

  public StmtFor getForStmt() {
    return forStmt;
  }

  public Stmt_if getIfStmt() {
    return ifStmt;
  }

  @Override
  public String toString() {
    if (base == StatementBase.SIF) {
      return ifStmt.toString();
    }
    if (base == StatementBase.SEXPR) {
      return exprStmt.toString() + ";\n";
    }
    if (base == StatementBase.SBLOCK) {
      return bloskStmt.toString();
    }
    if (base == StatementBase.SRETURN) {
      if (exprStmt != null) {
        return "return " + exprStmt.toString() + ";\n";
      } else {
        return "return;\n";
      }
    }
    if (base == StatementBase.SFOR) {
      return forStmt.toString();
    }
    return base.toString();
  }

}
