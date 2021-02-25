package _st3_linearize_expr.leaves;

public class Unop {
  private final String op;
  private final Var operand;

  public Unop(String op, Var operand) {
    this.op = op;
    this.operand = operand;
  }

  public String getOp() {
    return op;
  }

  public Var getOperand() {
    return operand;
  }

  @Override
  public String toString() {
    return op + operand.toString();
  }

}
