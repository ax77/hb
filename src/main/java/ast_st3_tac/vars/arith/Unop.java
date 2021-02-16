package ast_st3_tac.vars.arith;

import ast_st3_tac.vars.store.Value;

public class Unop {
  private final String op;
  private final Value operand;

  public Unop(String op, Value operand) {
    this.op = op;
    this.operand = operand;
  }

  public String getOp() {
    return op;
  }

  public Value getOperand() {
    return operand;
  }

  @Override
  public String toString() {
    return op + operand.toString();
  }

}
