package ast_printers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ast_types.Type;

public abstract class TypePrinters {

  static class TypeNamePair {
    private final Type tp;
    private final String name;

    public Type getTp() {
      return tp;
    }

    public String getName() {
      return name;
    }

    public TypeNamePair(Type tp, String name) {
      this.tp = tp;
      this.name = name;
    }
  }

  private static int counter = 1024;
  private static final List<TypeNamePair> pairs = new ArrayList<>();

  public static String genName(Type forType) {
    for (TypeNamePair pair : pairs) {
      if (pair.getTp().isEqualTo(forType)) {
        return pair.getName();
      }
    }
    String newname = String.format("%d", counter++);
    pairs.add(new TypeNamePair(forType, newname));
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
