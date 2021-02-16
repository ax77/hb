package ast_st3_tac.vars.arith;

import ast_st3_tac.vars.store.ERvalue;

public class Unop {
  private final String op;
  private final ERvalue operand;

  public Unop(String op, ERvalue operand) {
    this.op = op;
    this.operand = operand;
  }

  public String getOp() {
    return op;
  }

  public ERvalue getOperand() {
    return operand;
  }

  @Override
  public String toString() {
    return op + operand.toString();
  }

}
