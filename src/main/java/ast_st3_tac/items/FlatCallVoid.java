package ast_st3_tac.items;

import java.util.List;

import ast_printers.GenericListPrinter;
import ast_st3_tac.leaves.Var;
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
