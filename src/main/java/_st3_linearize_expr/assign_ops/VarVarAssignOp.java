package _st3_linearize_expr.assign_ops;

import _st3_linearize_expr.leaves.Var;
import ast_types.Type;

public class VarVarAssignOp {

  // a = opAssign(a, b)

  private final String method;
  private final Type type;
  private final Var lvalueArg;
  private final Var rvalueArg;

  public VarVarAssignOp(String method, Type type, Var lvalueArg, Var rvalueArg) {
    this.method = method;
    this.type = type;
    this.lvalueArg = lvalueArg;
    this.rvalueArg = rvalueArg;
  }

  public Type getType() {
    return type;
  }

  public Var getLvalueArg() {
    return lvalueArg;
  }

  public Var getRvalueArg() {
    return rvalueArg;
  }

  public String getMethod() {
    return method;
  }

  @Override
  public String toString() {
    return method + "(" + lvalueArg.toString() + ", " + rvalueArg + ")";
  }

}
