package ast_st3_tac.ir;

import ast_st3_tac.vars.store.Var;
import ast_types.Type;
import tokenize.Ident;

public class AuxCallAssignOpArgVar {

  // a = opAssign(a, b)

  private final Type type;
  private final Ident function;
  private final Var lvalueArg;
  private final Var rvalueArg;

  public AuxCallAssignOpArgVar(Type type, Ident function, Var lvalueArg, Var rvalueArg) {
    this.type = type;
    this.function = function;
    this.lvalueArg = lvalueArg;
    this.rvalueArg = rvalueArg;
  }

}
