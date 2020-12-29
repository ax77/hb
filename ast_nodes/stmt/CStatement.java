package njast.ast_nodes.stmt;

import jscan.tokenize.Token;
import njast.ast_kinds.CStatementBase;
import njast.ast_nodes.expr.Expression;

public class CStatement {

  //  <statement> ::= <statement without trailing substatement> | <labeled statement> | <if then statement> | <if then else statement> | <while statement> | <for statement>
  //
  //  <statement no short if> ::= <statement without trailing substatement> | <labeled statement no short if> | <if then else statement no short if> | <while statement no short if> | <for statement no short if>
  //
  //  <statement without trailing substatement> ::= <block> | <empty statement> | <expression statement> | <switch statement> | <do statement> | <break statement> | <continue statement> | <return statement> | <synchronized statement> | <throws statements> | <try statement>
  //
  //  <empty statement> ::= ;

  private final CStatementBase base;

  private Block compound;

  // return expr
  // expr-stmt
  private Expression expr;

  // return expr
  // return ;
  // expr-stmt
  public CStatement(Token from, CStatementBase base, Expression expr) {
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

  public Expression getExpr() {
    return expr;
  }

  public void setExpr(Expression expr) {
    this.expr = expr;
  }

  public CStatementBase getBase() {
    return base;
  }

}
