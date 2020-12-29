package njast.ast_nodes.stmt;

import njast.ast_nodes.clazz.vars.VarDeclarationLocal;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class StmtBlockStatement implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }
  //  <block statement> ::= <local variable declaration statement> | <statement>
  //
  //  <local variable declaration statement> ::= <local variable declaration> ;
  //
  //  <local variable declaration> ::= <type> <variable declarators>
  //
  //  <statement> ::= <statement without trailing substatement> | <labeled statement> | <if then statement> | <if then else statement> | <while statement> | <for statement>

  private VarDeclarationLocal localVars;
  private StmtStatement statement;

  public StmtBlockStatement(VarDeclarationLocal localVars) {
    this.localVars = localVars;
  }

  public StmtBlockStatement(StmtStatement statement) {
    this.statement = statement;
  }

  public VarDeclarationLocal getLocalVars() {
    return localVars;
  }

  public void setLocalVars(VarDeclarationLocal localVars) {
    this.localVars = localVars;
  }

  public StmtStatement getStatement() {
    return statement;
  }

  public void setStatement(StmtStatement statement) {
    this.statement = statement;
  }

}
