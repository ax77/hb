package ast_st3_tac.assign_ops;

import ast_st3_tac.leaves.FieldAccess;
import ast_st3_tac.leaves.Var;
import ast_types.Type;
import ast_vars.VarBase;
import tokenize.Ident;

public class FieldVarAssignOp {

  // a.b = opAssign(a.b, c)

  private final Type type;
  private final Ident function;
  private final FieldAccess lvalueArg;
  private final Var rvalueArg;

  public FieldVarAssignOp(Type type, Ident function, FieldAccess lvalueArg, Var rvalueArg) {
    this.type = type;
    this.function = function;
    this.lvalueArg = lvalueArg;
    this.rvalueArg = rvalueArg;
  }

  public Type getType() {
    return type;
  }

  public Ident getFunction() {
    return function;
  }

  public FieldAccess getLvalueArg() {
    return lvalueArg;
  }

  public Var getRvalueArg() {
    return rvalueArg;
  }

  @Override
  public String toString() {
    return function.toString() + "(" + lvalueArg.toString() + ", " + rvalueArg + ")";
  }

}
