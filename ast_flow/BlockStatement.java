package njast.ast_flow;

import njast.ast_class.vars.LocalVarDeclaration;

public class BlockStatement {
  //  <block statement> ::= <local variable declaration statement> | <statement>
  //
  //  <local variable declaration statement> ::= <local variable declaration> ;
  //
  //  <local variable declaration> ::= <type> <variable declarators>
  //
  //  <statement> ::= <statement without trailing substatement> | <labeled statement> | <if then statement> | <if then else statement> | <while statement> | <for statement>

  private LocalVarDeclaration localVars;
  private CStatement statement;

  public BlockStatement(LocalVarDeclaration localVars) {
    this.localVars = localVars;
  }

  public BlockStatement(CStatement statement) {
    this.statement = statement;
  }

  public LocalVarDeclaration getLocalVars() {
    return localVars;
  }

  public void setLocalVars(LocalVarDeclaration localVars) {
    this.localVars = localVars;
  }

  public CStatement getStatement() {
    return statement;
  }

  public void setStatement(CStatement statement) {
    this.statement = statement;
  }

}
