package ast_printers;

import java.util.List;

import ast_expr.ExprExpression;

public abstract class ExprPrinters {

  public static String funcArgsToString(List<ExprExpression> list) {
    StringBuilder sb = new StringBuilder();
    sb.append("(");

    for (int i = 0; i < list.size(); i++) {
      ExprExpression param = list.get(i);
      sb.append(param.toString());
      if (i + 1 < list.size()) {
        sb.append(", ");
      }
    }

    sb.append(")");
    return sb.toString();
  }

}