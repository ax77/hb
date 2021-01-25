package ast.templates;

import java.util.List;

import ast.parse.AstParseException;
import ast.types.ArrayType;
import ast.types.Type;
import ast.types.TypeBindings;

public abstract class NameBuilder {

  public static String buildNewName(final Type from) {

    final String origNameOfTemplateClass = from.getClassType().getIdentifier().getName();
    final String classUniqueId = from.getClassType().getUniqueIdToString();
    final List<Type> typeArguments = from.getTypeArguments();

    StringBuilder sb = new StringBuilder();
    sb.append(origNameOfTemplateClass);
    sb.append("_");
    sb.append(classUniqueId);
    sb.append("_");
    sb.append(typeArgumentsToStringForGeneratedName(typeArguments));
    return sb.toString();
  }

  // move toString() here, to guarantee that all names for generated templates will be unique
  // and not depends on general toString() which we may change one day or other.
  //
  private static String typeToString(Type tp) {
    boolean isPrimitive = tp.is_primitive();
    boolean isReference = tp.is_class();
    boolean isArray = tp.is_array();
    boolean isOk = isPrimitive || isReference || isArray;

    if (!isOk) {
      throw new AstParseException("expect primitive or reference type for name-generator");
    }

    if (isPrimitive) {
      return TypeBindings.BIND_PRIMITIVE_TO_STRING.get(tp.getBase());
    }

    if (isArray) {
      ArrayType array = tp.getArrayType();
      return String.format("%d", array.getCount()) + "_arr"; // TODO:
    }

    return tp.getClassType().getIdentifier().getName();
  }

  private static String typeArgumentsToStringForGeneratedName(List<Type> typeArguments) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < typeArguments.size(); i++) {
      Type tp = typeArguments.get(i);
      sb.append(typeToString(tp));
      if (i + 1 < typeArguments.size()) {
        sb.append("_");
      }
    }
    return sb.toString();
  }
}
