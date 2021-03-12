package _st3_linearize_expr.assign_ops;

import _st3_linearize_expr.leaves.Var;
import ast_types.Type;

public class VarVarAssignOp {

  // a = opAssign(a, b)

  private final Type type;
  private final Var lvalueArg;
  private final Var rvalueArg;

  public VarVarAssignOp(Type type, Var lvalueArg, Var rvalueArg) {
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

  @Override
  public String toString() {
    return "ASSIGN(" + lvalueArg.toString() + ", " + rvalueArg + ", " + type.toString() + ")";
  }

}
