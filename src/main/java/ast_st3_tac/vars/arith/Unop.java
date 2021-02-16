package ast_st3_tac.vars.arith;

import ast_st3_tac.vars.store.Var;

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
