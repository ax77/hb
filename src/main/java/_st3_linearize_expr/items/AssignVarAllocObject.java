package _st3_linearize_expr.items;

import java.util.ArrayList;
import java.util.List;

import _st3_linearize_expr.VarCollector;
import _st3_linearize_expr.rvalues.Var;
import _st7_codeout.ToStringsInternal;
import ast_class.ClassDeclaration;
import ast_types.Type;

public class AssignVarAllocObject implements VarCollector {
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
    final ClassDeclaration clazz = typename.getClassTypeFromRef();
    final String headerToString = ToStringsInternal.classHeaderToString(clazz);

    return lvalue.typeNameToString() + " = (" + ToStringsInternal.typeToString(lvalue.getType())
        + ") hb_alloc( sizeof(struct " + headerToString + ") )";
  }

  @Override
  public List<Var> getAllVars() {
    List<Var> vars = new ArrayList<>();
    vars.add(lvalue);
    return vars;
  }

}
