package _st3_linearize_expr.leaves;

public class FieldAccess {
  private final Var object;
  private final Var field;

  public FieldAccess(Var object, Var field) {
    this.object = object;
    this.field = field;
  }

  public Var getObject() {
    return object;
  }

  public Var getField() {
    return field;
  }

  @Override
  public String toString() {
    return object.toString() + "->" + field.toString();
  }

}
