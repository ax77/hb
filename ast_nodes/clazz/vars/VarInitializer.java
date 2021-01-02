package njast.ast_nodes.clazz.vars;

import java.io.Serializable;

import njast.ast_nodes.expr.ExprExpression;

public class VarInitializer implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6217182193092395111L;
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
