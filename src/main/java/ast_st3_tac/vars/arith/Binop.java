package ast_st3_tac.vars.arith;

import ast_st3_tac.vars.store.Value;

public class Binop {
  private final Value lhs;
  private final String op;
  private final Value rhs;

  public Binop(Value lhs, String op, Value rhs) {
    this.lhs = lhs;
    this.op = op;
    this.rhs = rhs;
  }

  public Value getLhs() {
    return lhs;
  }

  public String getOp() {
    return op;
  }

  public Value getRhs() {
    return rhs;
  }

  @Override
  public String toString() {
    return lhs.toString() + op + rhs.toString();
  }

}
