package _st3_linearize_expr.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.leaves.Var;

public class AssignVarDefaultValueForType implements VarCollector {
  private final Var lvalue;
  private final Var rvalue;

  public AssignVarDefaultValueForType(Var lvalue, Var rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public Var getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    String rhs = rvalue.toString();
    return lvalue.typeNameToString() + " = " + rhs;
  }

  @Override
  public List<Var> getAllVars() {
    List<Var> vars = new ArrayList<>();
    vars.add(lvalue);
    vars.add(rvalue);
    return vars;
  }

}
