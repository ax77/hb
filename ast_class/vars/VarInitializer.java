package njast.ast_class.vars;

import njast.ast_flow.CExpression;

public class VarInitializer {
  private CExpression initializer;

  public VarInitializer(CExpression initializer) {
    this.initializer = initializer;
  }

  public CExpression getInitializer() {
    return initializer;
  }

  public void setInitializer(CExpression initializer) {
    this.initializer = initializer;
  }

}
