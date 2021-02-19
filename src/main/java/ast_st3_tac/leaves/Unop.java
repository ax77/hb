package ast_st3_tac.leaves;

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
