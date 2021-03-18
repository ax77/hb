package ast_printers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ast_types.Type;
import ast_types.TypeBase;

public abstract class TypePrinters {

  private static int counter = 1024;
  private static final Map<String, String> pairs = new HashMap<>();

  public static String genName(Type forType) {
    if (forType.is(TypeBase.TP_TYPENAME_ID)) {
      System.out.println("type-printer found the typename-id in the type-arguments, the class is not expanded...");
      return "";
    }
    if (pairs.containsKey(forType.toString())) {
      return pairs.get(forType.toString());
    }
    String newname = String.format("%d", counter++);
    pairs.put(forType.toString(), newname);
    return newname;
  }

  public static String typeArgumentsToString(List<Type> typeArguments) {
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < typeArguments.size(); i++) {
      Type tp = typeArguments.get(i);
      sb.append(genName(tp));
      if (i + 1 < typeArguments.size()) {
        sb.append("_");
      }
    }
    return sb.toString();
  }

}
