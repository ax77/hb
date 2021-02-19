package ast_st3_tac.calls;

import ast_st3_tac.leaves.Var;
import ast_types.Type;
import tokenize.Ident;

public class FlatCallAssignOpArgVar {

  // a = opAssign(a, b)

  private final Type type;
  private final Ident function;
  private final Var lvalueArg;
  private final Var rvalueArg;

  public FlatCallAssignOpArgVar(Type type, Ident function, Var lvalueArg, Var rvalueArg) {
    this.type = type;
    this.function = function;
    this.lvalueArg = lvalueArg;
    this.rvalueArg = rvalueArg;
  }

  @Override
  public String toString() {
    return function.toString() + "(" + lvalueArg.toString() + ", " + rvalueArg + ")";
  }

}
