package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.Var;
import ast_types.Type;

public class AssignVarCastExpression {
  private final Var lvalue;
  private final Var rvalue;
  private final Type type;

  public AssignVarCastExpression(Var lvalue, Var rvalue, Type type) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
    this.type = type;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public Var getRvalue() {
    return rvalue;
  }

  public Type getType() {
    return type;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = ((" + type.toString() + ")" + rvalue.toString() + ")";
  }

}
