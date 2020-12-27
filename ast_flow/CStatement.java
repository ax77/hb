package njast.ast_flow;

import jscan.tokenize.Token;
import njast.ast_class.Block;

public class CStatement {

  private final CStatementBase base;

  private Block compound;

  // return expr
  // expr-stmt
  private CExpression expr;

  // return expr
  // return ;
  // expr-stmt
  public CStatement(Token from, CStatementBase base, CExpression expr) {
    this.base = base;
    this.expr = expr;
  }

  // {  }
  public CStatement(Token from, Block compound) {
    this.base = CStatementBase.SBLOCK;
    this.compound = compound;
  }

  public Block getCompound() {
    return compound;
  }

  public void setCompound(Block compound) {
    this.compound = compound;
  }

  public CExpression getExpr() {
    return expr;
  }

  public void setExpr(CExpression expr) {
    this.expr = expr;
  }

  public CStatementBase getBase() {
    return base;
  }

}
