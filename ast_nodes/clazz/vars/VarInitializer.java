package njast.ast_nodes.clazz.vars;

import njast.ast_nodes.expr.ExpressionNode;

public class VarInitializer {
  private ExpressionNode initializer;

  public VarInitializer(ExpressionNode initializer) {
    this.initializer = initializer;
  }

  public ExpressionNode getInitializer() {
    return initializer;
  }

  public void setInitializer(ExpressionNode initializer) {
    this.initializer = initializer;
  }

}
