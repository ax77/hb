package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.Var;
import ast_class.ClassDeclaration;
import ast_printers.TypePrinters;
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

  private String classHeaderToString(ClassDeclaration clazz) {
    StringBuilder sb = new StringBuilder();
    sb.append(clazz.getIdentifier().getName());
    if (!clazz.getTypeParametersT().isEmpty()) {
      sb.append("_");
      sb.append(TypePrinters.typeArgumentsToString(clazz.getTypeParametersT()));
    }
    return sb.toString();
  }

  @Override
  public String toString() {
    /// if (typename.isBytes()) {
    ///   int sz = typename.getSize();
    ///   return lvalue.typeNameToString() + " = hmalloc(sizeof(char) * " + String.format("%d", sz) + "))";
    /// }
    final ClassDeclaration clazz = typename.getClassTypeFromRef();
    return lvalue.typeNameToString() + " = hmalloc(sizeof(struct " + classHeaderToString(clazz) + "))";
  }

}
