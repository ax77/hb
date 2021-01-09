package njast.templates;

import java.util.List;

import njast.errors.EParseException;
import njast.types.Type;
import njast.types.TypeBindings;

public abstract class NameBuilder {

  public static String buildNewName(final Type from) {

    final String origNameOfTemplateClass = from.getClassType().getIdentifier().getName();
    final String classUniqueId = from.getClassType().getUniqueIdStr();
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
    if (tp.isPrimitive()) {
      return TypeBindings.BIND_PRIMITIVE_TO_STRING.get(tp.getBase());
    }
    if (tp.isTypeVarRef()) {
      return tp.getTypeVariable().getName();
    }
    if (!tp.isClassRef()) {
      throw new EParseException("expect class-name");
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
