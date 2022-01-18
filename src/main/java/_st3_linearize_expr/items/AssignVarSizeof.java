package _st3_linearize_expr.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.VarCollector;
import _st3_linearize_expr.rvalues.Var;
import ast_types.Type;

public class AssignVarSizeof implements VarCollector {
  private final Var lvalue;
  private final Type typename;

  public AssignVarSizeof(Var lvalue, Type typename) {
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
    return lvalue.typeNameToString() + " = sizeof(" + typename.toString() + ")";
  }

  @Override
  public List<Var> getAllVars() {
    List<Var> vars = new ArrayList<>();
    vars.add(lvalue);
    return vars;
  }

}
