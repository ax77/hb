package njast.ast_flow.expr;

import jscan.symtab.Ident;
import jscan.tokenize.Token;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class FieldAccess implements AstTraverser {
  private final Ident name;
  private final CExpression expression;

  public FieldAccess(Ident name, CExpression expression) {
    this.name = name;
    this.expression = expression;
  }

  public Ident getName() {
    return name;
  }

  public CExpression getExpression() {
    return expression;
  }
  
  
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

}
