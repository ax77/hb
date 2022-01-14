package _st3_linearize_expr.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.leaves.FunctionCallWithResult;
import _st3_linearize_expr.leaves.Var;

public class AssignVarFlatCallClassCreationTmp implements VarCollector {
  private final Var lvalue;
  private final FunctionCallWithResult rvalue;

  public AssignVarFlatCallClassCreationTmp(Var lvalue, FunctionCallWithResult rvalue) {
    this.lvalue = lvalue;
    this.rvalue = rvalue;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public FunctionCallWithResult getRvalue() {
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
    vars.addAll(rvalue.getAllVars());
    return vars;
  }
}
