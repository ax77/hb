package ast_printers;

import java.util.List;

public class GenericListPrinter {

  public static <E> String paramsToStringWithBraces(List<E> args) {
    StringBuilder sb = new StringBuilder();
    sb.append("(");

    for (int i = 0; i < args.size(); i++) {
      E param = args.get(i);
      sb.append(param.toString());

      if (i + 1 < args.size()) {
        sb.append(", ");
      }
    }

    sb.append(")");
    return sb.toString();
  }

}
