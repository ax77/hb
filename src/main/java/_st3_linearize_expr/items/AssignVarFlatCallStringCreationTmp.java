package _st3_linearize_expr.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.leaves.FunctionCallWithResult;
import _st3_linearize_expr.leaves.Var;

public class AssignVarFlatCallStringCreationTmp implements VarCollector {
  private final Var lvalue;
  private final FunctionCallWithResult constructor;
  private final String rvalue;

  public AssignVarFlatCallStringCreationTmp(Var lvalue, String rvalue, FunctionCallWithResult constructor) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
    this.constructor = constructor;
  }

  public FunctionCallWithResult getConstructor() {
    return constructor;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public String getRvalue() {
    return rvalue;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + rvalue.toString();
  }

  @Override
  public List<Var> getAllVars() {
    List<Var> vars = new ArrayList<>();
    vars.add(lvalue);
    vars.addAll(constructor.getAllVars());
    return vars;
  }

}
