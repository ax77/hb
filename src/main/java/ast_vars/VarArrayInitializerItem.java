package ast_vars;

import java.io.Serializable;

import ast_expr.ExprExpression;

public class VarArrayInitializerItem implements Serializable {
  private static final long serialVersionUID = -794305928505278466L;

  private final ExprExpression init;
  private final int offset;

  public VarArrayInitializerItem(ExprExpression init, int offset) {
    this.init = init;
    this.offset = offset;
  }

  public ExprExpression getInit() {
    return init;
  }

  public int getOffset() {
    return offset;
  }

  @Override
  public String toString() {
    return String.format("%d", offset) + " = " + init.toString();
  }
}
