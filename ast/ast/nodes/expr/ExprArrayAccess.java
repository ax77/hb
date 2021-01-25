package ast.ast.nodes.expr;

public class ExprArrayAccess {
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
