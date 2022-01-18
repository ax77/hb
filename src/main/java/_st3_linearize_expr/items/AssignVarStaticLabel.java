package _st3_linearize_expr.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.VarCollector;
import _st3_linearize_expr.rvalues.Var;
import tokenize.Ident;

public class AssignVarStaticLabel implements VarCollector {
  private final Var lvalue;
  private final Ident staticClassName;
  private final Ident staticClassFieldName;

  public AssignVarStaticLabel(Var lvalue, Ident staticClassName, Ident staticClassFieldName) {
    this.lvalue = lvalue;
    this.staticClassName = staticClassName;
    this.staticClassFieldName = staticClassFieldName;
  }

  public Var getLvalue() {
    return lvalue;
  }

  public Ident getStaticClassName() {
    return staticClassName;
  }

  public Ident getStaticClassFieldName() {
    return staticClassFieldName;
  }

  @Override
  public String toString() {
    return lvalue.typeNameToString() + " = " + staticClassName + "_" + staticClassFieldName;
  }

  @Override
  public List<Var> getAllVars() {
    List<Var> vars = new ArrayList<>();
    vars.add(lvalue);
    return vars;
  }
}
