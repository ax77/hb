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

  public static <E> String commaListNoBraces(List<E> list) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < list.size(); i++) {
      E elem = list.get(i);
      sb.append(elem.toString());

      if (i + 1 < list.size()) {
        sb.append(", ");
      }
    }

    return sb.toString();
  }

}
