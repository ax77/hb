package ast_st5_stmts.execs;

import ast_st3_tac.LinearExpression;

public class LinearContinue {
  private final LinearLoop loop;

  private LinearExpression step;
  private LocalDestructors destructors;

  public LinearContinue(LinearLoop loop) {
    this.loop = loop;
  }

  @Override
  public String toString() {
    return "continue;";
  }

}
