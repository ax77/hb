package ast_types;

import java.util.List;

public abstract class TypePrinters {
  public static String typeArgumentsToString(List<Type> typeArguments) {
    final StringBuilder sb = new StringBuilder();
    sb.append("<");
    for (int i = 0; i < typeArguments.size(); i++) {
      Type tp = typeArguments.get(i);
      sb.append(tp.toString());
      if (i + 1 < typeArguments.size()) {
        sb.append(", ");
      }
    }
    sb.append(">");
    return sb.toString();
  }
}
