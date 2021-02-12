package ast_stmt;

import java.io.Serializable;

import ast_expr.ExprExpression;
import ast_sourceloc.Location;
import ast_sourceloc.SourceLocation;
import tokenize.Token;

public class StmtStatement implements Serializable, Location {
  private static final long serialVersionUID = 2946438995245230886L;

  private final StatementBase base;
  private final Token beginPos;
  private StmtBlock bloskStmt;
  private ExprExpression exprStmt;
  private StmtForeach foreachStmt;
  private StmtSelect ifStmt;
  private StmtWhile whileStmt;
  private StmtFor forStmt;
  private StmtReturn returnStmt;

  public StmtStatement(StmtFor forStmt, Token beginPos) {
    this.base = StatementBase.SFOR;
    this.beginPos = beginPos;
    this.forStmt = forStmt;
  }

  public StmtStatement(StmtReturn returnStmt, Token beginPos) {
    this.base = StatementBase.SRETURN;
    this.beginPos = beginPos;
    this.returnStmt = returnStmt;
  }

  public StmtStatement(StmtWhile whileStmt, Token beginPos) {
    this.base = StatementBase.SWHILE;
    this.beginPos = beginPos;
    this.whileStmt = whileStmt;
  }

  // it will be rewritten to while() at the stage-2
  public StmtStatement(StmtForeach foreachStmt, Token beginPos) {
    this.base = StatementBase.SFOREACH_TMP;
    this.beginPos = beginPos;
    this.foreachStmt = foreachStmt;
  }

  public StmtStatement(StmtSelect ifStmt, Token beginPos) {
    this.base = StatementBase.SIF;
    this.beginPos = beginPos;
    this.ifStmt = ifStmt;
  }

  // <expr> 
  public StmtStatement(ExprExpression exprStmt, Token beginPos) {
    this.base = StatementBase.SEXPR;
    this.beginPos = beginPos;
    this.exprStmt = exprStmt;
  }

  // {  }
  public StmtStatement(StmtBlock bloskStmt, Token beginPos) {
    this.base = StatementBase.SBLOCK;
    this.beginPos = beginPos;
    this.bloskStmt = bloskStmt;
  }

  public StmtReturn getReturnStmt() {
    return returnStmt;
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

  public StmtForeach getForeachStmt() {
    return foreachStmt;
  }

  public StmtFor getForStmt() {
    return forStmt;
  }

  public StmtSelect getIfStmt() {
    return ifStmt;
  }

  public StmtWhile getWhileStmt() {
    return whileStmt;
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
      return returnStmt.toString();
    }
    if (base == StatementBase.SFOREACH_TMP) {
      return foreachStmt.toString();
    }
    if (base == StatementBase.SWHILE) {
      return whileStmt.toString();
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
