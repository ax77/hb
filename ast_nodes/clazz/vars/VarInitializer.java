package njast.ast_nodes.clazz.vars;

import njast.ast_nodes.expr.ExprExpression;

public class VarInitializer {

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
