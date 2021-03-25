package ast_expr;

import java.io.Serializable;
import java.util.List;

import ast_printers.GenericListPrinter;
import tokenize.Ident;

public class ExprBuiltinFunc implements Serializable {
  private static final long serialVersionUID = 6082427317484452898L;

  private final Ident name;
  private final List<ExprExpression> args;

  public ExprBuiltinFunc(Ident name, List<ExprExpression> args) {
    this.name = name;
    this.args = args;
  }

  public Ident getName() {
    return name;
  }

  public List<ExprExpression> getArgs() {
    return args;
  }

  @Override
  public String toString() {
    return name.toString() + GenericListPrinter.paramsToStringWithBraces(args);
  }

}
