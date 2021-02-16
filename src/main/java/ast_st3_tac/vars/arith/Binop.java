package ast_st3_tac.vars.arith;

import ast_st3_tac.vars.store.Var;

public class Binop {
  private final Var lhs;
  private final String op;
  private final Var rhs;

  public Binop(Var lhs, String op, Var rhs) {
    this.lhs = lhs;
    this.op = op;
    this.rhs = rhs;
  }

  public Var getLhs() {
    return lhs;
  }

  public String getOp() {
    return op;
  }

  public Var getRhs() {
    return rhs;
  }

  @Override
  public String toString() {
    return lhs.toString() + " " + op + " " + rhs.toString();
  }

}
