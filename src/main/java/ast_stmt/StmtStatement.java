package ast_stmt;

import java.io.Serializable;

import ast_expr.ExprExpression;
import ast_sourceloc.Location;
import ast_sourceloc.SourceLocation;
import tokenize.Token;

public class StmtStatement implements Serializable, Location {
  private static final long serialVersionUID = 2946438995245230886L;

  private /*final*/ StatementBase base;
  private final Token beginPos;
  private StmtBlock bloskStmt;
  private ExprExpression exprStmt; // return expr or expr-stmt
  private StmtForeach forEachStmt;
  private StmtSelect ifStmt;
  private StmtWhile whileStmt;

  //MIR:TREE:rewriter
  public void replaceForLoopWithBlock(StmtBlock block) {
    this.base = StatementBase.SBLOCK;
    this.bloskStmt = block;
    this.forEachStmt = null;
  }

  public StmtStatement(StmtWhile whileStmt, Token beginPos) {
    this.base = StatementBase.SWHILE;
    this.beginPos = beginPos;
    this.whileStmt = whileStmt;
  }

  // it will be rewritten to while() at the stage-2
  public StmtStatement(StmtForeach forEachStmt, Token beginPos) {
    this.base = StatementBase.SFOREACH_TMP;
    this.beginPos = beginPos;
    this.forEachStmt = forEachStmt;
  }

  public StmtStatement(StmtSelect ifStmt, Token beginPos) {
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

  public StmtForeach getForStmt() {
    return forEachStmt;
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
      if (exprStmt != null) {
        return "return " + exprStmt.toString() + ";\n";
      } else {
        return "return;\n";
      }
    }
    if (base == StatementBase.SFOREACH_TMP) {
      return forEachStmt.toString();
    }
    if (base == StatementBase.SWHILE) {
      return whileStmt.toString();
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
