package njast.ast_nodes.expr;

import jscan.symtab.Ident;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class FieldAccess implements AstTraverser {
  private final Ident name;
  private final ExpressionNode expression;

  public FieldAccess(Ident name, ExpressionNode expression) {
    this.name = name;
    this.expression = expression;
  }

  public Ident getName() {
    return name;
  }

  public ExpressionNode getExpression() {
    return expression;
  }

  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

}
