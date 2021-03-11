package _st3_linearize_expr.items;

import _st3_linearize_expr.leaves.Var;
import ast_class.ClassDeclaration;
import ast_printers.TypePrinters;
import ast_types.Type;
import tokenize.Ident;

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
    final ClassDeclaration clazz = typename.getClassTypeFromRef();
    final Ident identifier = clazz.getIdentifier();

    if (clazz.isNativeArray()) {
      // return lvalue.typeNameToString() + " = vec_new(" + clazz.getTypeParametersT().get(0).toString() + ")";
    }

    return lvalue.typeNameToString() + " = hmalloc(sizeof(struct " + classHeaderToString(clazz) + "))";
  }

}
