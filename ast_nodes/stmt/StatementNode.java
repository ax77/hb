package njast.ast_nodes.stmt;

import jscan.tokenize.Token;
import njast.ast_kinds.StatementBase;
import njast.ast_nodes.expr.ExpressionNode;

public class StatementNode {

  //  <statement> ::= <statement without trailing substatement> | <labeled statement> | <if then statement> | <if then else statement> | <while statement> | <for statement>
  //
  //  <statement no short if> ::= <statement without trailing substatement> | <labeled statement no short if> | <if then else statement no short if> | <while statement no short if> | <for statement no short if>
  //
  //  <statement without trailing substatement> ::= <block> | <empty statement> | <expression statement> | <switch statement> | <do statement> | <break statement> | <continue statement> | <return statement> | <synchronized statement> | <throws statements> | <try statement>
  //
  //  <empty statement> ::= ;

  private final StatementBase base;
  private Block compound;
  private Return sreturn;
  private ExpressionNode sexpression;

  public StatementNode(ExpressionNode sexpression) {
    this.base = StatementBase.SEXPR;
    this.sexpression = sexpression;
  }

  public StatementNode(Return sreturn) {
    this.base = StatementBase.SRETURN;
    this.sreturn = sreturn;
  }

  // {  }
  public StatementNode(Token from, Block compound) {
    this.base = StatementBase.SBLOCK;
    this.compound = compound;
  }

  public Block getCompound() {
    return compound;
  }

  public void setCompound(Block compound) {
    this.compound = compound;
  }

  public StatementBase getBase() {
    return base;
  }

  public Return getSreturn() {
    return sreturn;
  }

  public ExpressionNode getSexpression() {
    return sexpression;
  }

}
