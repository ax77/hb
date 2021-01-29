package ast_st1_templates;

import java.util.List;

import ast_class.ClassDeclaration;
import ast_types.ArrayType;
import ast_types.ClassType;
import ast_types.Type;
import ast_types.TypeBindings;
import errors.AstParseException;

public abstract class NameBuilder {

  /// new name will be generated as [class_name+type_parameters]
  /// and each type-parameter will be generated recursively also
  /// so: for [opt<opt<i32>>] we'll have [opt_opt_i32] and so on
  ///
  public static String buildNewName(final Type from) {
    return classTypeRefToString(from.getClassTypeRef());
  }

  /// move toString() here, to guarantee that all names for generated templates will be unique
  /// and does not depend on general toString() which we may change one day or another.
  ///
  private static String typeToString(Type tp) {
    final boolean isPrimitive = tp.is_primitive();
    final boolean isReference = tp.is_class();
    final boolean isArray = tp.is_array();
    final boolean isOk = isPrimitive || isReference || isArray;

    if (!isOk) {
      throw new AstParseException("expect primitive or reference type for name-generator");
    }

    if (isPrimitive) {
      return TypeBindings.BIND_PRIMITIVE_TO_STRING.get(tp.getBase());
    }

    if (isArray) {
      return arrayToString(tp);
    }

    return classTypeRefToString(tp.getClassTypeRef());
  }

  private static String classTypeRefToString(final ClassType classTypeRef) {
    final ClassDeclaration clazz = classTypeRef.getClazz();
    final StringBuilder sb = new StringBuilder();
    sb.append(clazz.getIdentifier().getName());
    sb.append("_");
    sb.append(typeArgumentsToString(classTypeRef.getTypeArguments()));
    return sb.toString();
  }

  private static String arrayToString(Type tp) {
    final ArrayType array = tp.getArrayType();
    final StringBuilder sb = new StringBuilder();
    sb.append("array_of_");
    final int count = array.getCount();
    if (count > 0) {
      sb.append(String.format("%d", count));
      sb.append("_");
    }
    sb.append(typeToString(array.getArrayOf()));
    return sb.toString();
  }

  private static String typeArgumentsToString(List<Type> typeArgs) {
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < typeArgs.size(); i++) {
      Type tp = typeArgs.get(i);
      sb.append(typeToString(tp));
      if (i + 1 < typeArgs.size()) {
        sb.append("_");
      }
    }
    return sb.toString();
  }
}
