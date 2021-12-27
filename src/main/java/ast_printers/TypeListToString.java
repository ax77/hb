package ast_printers;

import java.util.List;

import ast_types.Type;

public abstract class TypeListToString {
  public static String gen(List<Type> types) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < types.size(); i += 1) {
      Type tp = types.get(i);
      sb.append(tp.toString());
      if (i + 1 < types.size()) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }
}
