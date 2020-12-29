package njast.ast_nodes.clazz.vars;

import njast.ast_nodes.expr.Expression;

public class VarInitializer {
  private Expression initializer;

  public VarInitializer(Expression initializer) {
    this.initializer = initializer;
  }

  public Expression getInitializer() {
    return initializer;
  }

  public void setInitializer(Expression initializer) {
    this.initializer = initializer;
  }

}
