package njast.ast_nodes.stmt;

import java.util.List;

import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class StmtBlockItem implements AstTraverser {
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

  private List<VarDeclarator> localVars;
  private StmtStatement statement;

  public StmtBlockItem(List<VarDeclarator> localVars) {
    this.localVars = localVars;
  }

  public StmtBlockItem(StmtStatement statement) {
    this.statement = statement;
  }

  public List<VarDeclarator> getLocalVars() {
    return localVars;
  }

  public void setLocalVars(List<VarDeclarator> localVars) {
    this.localVars = localVars;
  }

  public StmtStatement getStatement() {
    return statement;
  }

  public void setStatement(StmtStatement statement) {
    this.statement = statement;
  }

}
