package ast_stmt;

import java.io.Serializable;

import ast_expr.ExprExpression;
import ast_sourceloc.ILocation;
import ast_sourceloc.SourceLocation;
import tokenize.Token;

public class StmtStatement implements Serializable, ILocation {
  private static final long serialVersionUID = 2946438995245230886L;

  private /*final*/ StatementBase base;
  private final Token beginPos;
  private StmtBlock bloskStmt;
  private ExprExpression exprStmt; // return expr or expr-stmt
  private Stmt_for forStmt;
  private Stmt_if ifStmt;

  //MIR:TREE:rewriter
  public void replaceForLoopWithBlock(StmtBlock block) {
    this.base = StatementBase.SBLOCK;
    this.bloskStmt = block;
    this.forStmt = null;
  }

  public StmtStatement(Stmt_for forStmt, Token beginPos) {
    this.base = StatementBase.SFOR;
    this.beginPos = beginPos;

    this.forStmt = forStmt;
  }

  public StmtStatement(Stmt_if ifStmt, Token beginPos) {
    this.base = StatementBase.SIF;
    this.beginPos = beginPos;

    this.ifStmt = ifStmt;
  }

  // return <expr> ;
  // <expr> 
  public StmtStatement(StatementBase base, ExprExpression exprStmt, Token beginPos) {
    this.base = base;
    this.beginPos = beginPos;

    this.exprStmt = exprStmt;
  }

  // {  }
  public StmtStatement(StmtBlock bloskStmt, Token beginPos) {
    this.base = StatementBase.SBLOCK;
    this.beginPos = beginPos;

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

  public Stmt_for getForStmt() {
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

  @Override
  public SourceLocation getLocation() {
    return beginPos.getLocation();
  }

  @Override
  public String getLocationToString() {
    return beginPos.getLocationToString();
  }

  public Token getBeginPos() {
    return beginPos;
  }

}
