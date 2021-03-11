package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.Var;

public class AssignVarFlatCallStringCreationTmp {
  private final Var lvalue;
  private final String rvalue;

  private final String constructor;
  private final String appendMethod;

  public AssignVarFlatCallStringCreationTmp(Var lvalue, String rvalue, String constructor, String appendMethod) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
    this.constructor = constructor;
    this.appendMethod = appendMethod;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public String getRvalue() {
    return rvalue;
  }

  public String getConstructor() {
    return constructor;
  }

  public String getAppendMethod() {
    return appendMethod;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.toString();
  }

}
