package _st3_linearize_expr.items;

import java.util.List;

import _st3_linearize_expr.leaves.Var;
import ast_printers.GenericListPrinter;
import tokenize.Ident;

public class FlatCallVoid {
  private final Ident function;
  private final List<Var> args;

  public FlatCallVoid(Ident function, List<Var> args) {
    this.function = function;
    this.args = args;
  }

  public Ident getFunction() {
    return function;
  }

  public List<Var> getArgs() {
    return args;
  }

  @Override
  public String toString() {
    return function.toString() + GenericListPrinter.paramsToStringWithBraces(args);
  }

}
