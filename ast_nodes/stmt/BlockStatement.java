package njast.ast_nodes.stmt;

import njast.ast_nodes.clazz.vars.LocalVarDeclaration;

public class BlockStatement {
  //  <block statement> ::= <local variable declaration statement> | <statement>
  //
  //  <local variable declaration statement> ::= <local variable declaration> ;
  //
  //  <local variable declaration> ::= <type> <variable declarators>
  //
  //  <statement> ::= <statement without trailing substatement> | <labeled statement> | <if then statement> | <if then else statement> | <while statement> | <for statement>

  private LocalVarDeclaration localVars;
  private StatementNode statement;

  public BlockStatement(LocalVarDeclaration localVars) {
    this.localVars = localVars;
  }

  public BlockStatement(StatementNode statement) {
    this.statement = statement;
  }

  public LocalVarDeclaration getLocalVars() {
    return localVars;
  }

  public void setLocalVars(LocalVarDeclaration localVars) {
    this.localVars = localVars;
  }

  public StatementNode getStatement() {
    return statement;
  }

  public void setStatement(StatementNode statement) {
    this.statement = statement;
  }

}
