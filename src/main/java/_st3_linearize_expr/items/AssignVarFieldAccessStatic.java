package _st3_linearize_expr.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.leaves.FieldAccess;
import _st3_linearize_expr.leaves.Var;

public class AssignVarFieldAccessStatic implements VarCollector {
  private final Var lvalue;
  private final FieldAccess rvalue;

  public AssignVarFieldAccessStatic(Var lvalue, FieldAccess rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public FieldAccess getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.getObject().getName() + "_" + rvalue.getField().getName();
  }

  @Override
  public List<Var> getAllVars() {
    List<Var> vars = new ArrayList<>();
    vars.add(lvalue);
    vars.addAll(rvalue.getAllVars());
    return vars;
  }
}
