package ast_printers;

import java.util.List;

import ast_expr.ExprExpression;

public abstract class ExprPrinters {

  public static String funcArgsToString(List<ExprExpression> list) {
    return GenericListPrinter.paramsToStringWithBraces(list);
  }

}