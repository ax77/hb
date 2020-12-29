package njast.ast_nodes.clazz.vars;

import njast.ast_nodes.expr.ExprExpression;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class VarInitializer implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

  private ExprExpression initializer;

  public VarInitializer(ExprExpression initializer) {
    this.initializer = initializer;
  }

  public ExprExpression getInitializer() {
    return initializer;
  }

  public void setInitializer(ExprExpression initializer) {
    this.initializer = initializer;
  }

}
