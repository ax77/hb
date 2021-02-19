package ast_st3_tac.calls;

import ast_st3_tac.leaves.ArrayAccess;
import ast_st3_tac.leaves.Var;
import ast_types.Type;
import tokenize.Ident;

public class FlatCallAssignOpArgArray {

  // a[b] = opAssign(a[b], c)

  private final Type type;
  private final Ident function;
  private final ArrayAccess lvalueArg;
  private final Var rvalueArg;

  public FlatCallAssignOpArgArray(Type type, Ident function, ArrayAccess lvalueArg, Var rvalueArg) {
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

  public ArrayAccess getLvalueArg() {
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
