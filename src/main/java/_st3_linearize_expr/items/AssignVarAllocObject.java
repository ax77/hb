package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.Var;
import ast_types.Type;

public class AssignVarAllocObject {
  private final Var lvalue;
  private final Type typename;

  public AssignVarAllocObject(Var lvalue, Type typename) {
    this.lvalue = lvalue;
    this.typename = typename;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public Type getTypename() {
    return typename;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = new " + typename.getClassTypeFromRef().getIdentifier().toString() + "()";
  }

}
