package _st3_linearize_expr.assign_ops;

import _st3_linearize_expr.leaves.ArrayAccess;
import _st3_linearize_expr.leaves.Var;
import ast_types.Type;
import tokenize.Ident;

public class ArrayVarAssignOp {

  // a[b] = opAssign(a[b], c)

  private final Type type;
  private final Ident function;
  private final ArrayAccess lvalueArg;
  private final Var rvalueArg;

  public ArrayVarAssignOp(Type type, Ident function, ArrayAccess lvalueArg, Var rvalueArg) {
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
