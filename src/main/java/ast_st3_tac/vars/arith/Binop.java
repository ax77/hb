package ast_st3_tac.vars.arith;

import ast_st3_tac.vars.store.ERvalue;

public class Binop {
  private final ERvalue lhs;
  private final String op;
  private final ERvalue rhs;

  public Binop(ERvalue lhs, String op, ERvalue rhs) {
    this.lhs = lhs;
    this.op = op;
    this.rhs = rhs;
  }

  public ERvalue getLhs() {
    return lhs;
  }

  public String getOp() {
    return op;
  }

  public ERvalue getRhs() {
    return rhs;
  }

  @Override
  public String toString() {
    return lhs.toString() + " " + op + " " + rhs.toString();
  }

}
