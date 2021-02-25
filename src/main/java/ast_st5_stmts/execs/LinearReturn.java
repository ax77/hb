package ast_st5_stmts.execs;

import ast_st3_tac.leaves.Var;

public class LinearReturn {

  private final Var result;
  private LocalDestructors destructors;

  public LinearReturn(Var result) {
    this.result = result;
  }

  @Override
  public String toString() {
    return "return " + (result != null ? result.toString() : "") + ";";
  }

}
