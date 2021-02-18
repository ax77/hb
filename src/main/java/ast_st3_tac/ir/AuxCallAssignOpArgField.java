package ast_st3_tac.ir;

import ast_st3_tac.vars.store.FieldAccess;
import ast_st3_tac.vars.store.Var;
import ast_types.Type;
import tokenize.Ident;

public class AuxCallAssignOpArgField {

  // a.b = opAssign(a.b, c)

  private final Type type;
  private final Ident function;
  private final FieldAccess lvalueArg;
  private final Var rvalueArg;

  public AuxCallAssignOpArgField(Type type, Ident function, FieldAccess lvalueArg, Var rvalueArg) {
    this.type = type;
    this.function = function;
    this.lvalueArg = lvalueArg;
    this.rvalueArg = rvalueArg;
  }

}
