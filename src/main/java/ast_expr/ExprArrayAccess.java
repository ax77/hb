package ast_expr;

import java.io.Serializable;

public class ExprArrayAccess implements Serializable {
  private static final long serialVersionUID = -3791155341811163083L;

  private final ExprExpression array;
  private final ExprExpression index;

  public ExprArrayAccess(ExprExpression array, ExprExpression index) {
    this.array = array;
    this.index = index;
  }

  public ExprExpression getArray() {
    return array;
  }

  public ExprExpression getIndex() {
    return index;
  }

  @Override
  public String toString() {
    return array.toString() + "[" + index.toString() + "]";
  }

}
