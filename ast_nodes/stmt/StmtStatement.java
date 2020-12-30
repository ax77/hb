package njast.ast_nodes.stmt;

import jscan.tokenize.Token;
import njast.ast_kinds.StatementBase;
import njast.ast_nodes.expr.ExprExpression;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class StmtStatement implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

  //  <statement> ::= <statement without trailing substatement> | <labeled statement> | <if then statement> | <if then else statement> | <while statement> | <for statement>
  //
  //  <statement no short if> ::= <statement without trailing substatement> | <labeled statement no short if> | <if then else statement no short if> | <while statement no short if> | <for statement no short if>
  //
  //  <statement without trailing substatement> ::= <block> | <empty statement> | <expression statement> | <switch statement> | <do statement> | <break statement> | <continue statement> | <return statement> | <synchronized statement> | <throws statements> | <try statement>
  //
  //  <empty statement> ::= ;

  private final StatementBase base;
  private StmtBlock compound;
  private StmtReturn sreturn;
  private ExprExpression sexpression;
  private StmtFor sfor;

  public StmtStatement(StmtFor sfor) {
    this.base = StatementBase.SFOR;
    this.sfor = sfor;
  }

  public StmtStatement(ExprExpression sexpression) {
    this.base = StatementBase.SEXPR;
    this.sexpression = sexpression;
  }

  public StmtStatement(StmtReturn sreturn) {
    this.base = StatementBase.SRETURN;
    this.sreturn = sreturn;
  }

  // {  }
  public StmtStatement(Token from, StmtBlock compound) {
    this.base = StatementBase.SBLOCK;
    this.compound = compound;
  }

  public StmtBlock getCompound() {
    return compound;
  }

  public void setCompound(StmtBlock compound) {
    this.compound = compound;
  }

  public StatementBase getBase() {
    return base;
  }

  public StmtReturn getSreturn() {
    return sreturn;
  }

  public ExprExpression getSexpression() {
    return sexpression;
  }

  public StmtFor getSfor() {
    return sfor;
  }

  @Override
  public String toString() {
    return base.toString();
  }

}
