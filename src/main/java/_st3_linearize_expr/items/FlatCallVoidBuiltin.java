package _st3_linearize_expr.items;

import java.util.List;

import _st3_linearize_expr.leaves.Var;
import ast_printers.GenericListPrinter;
import tokenize.Ident;

public class FlatCallVoidBuiltin {

  private final Ident fullname;
  private final List<Var> args;

  public FlatCallVoidBuiltin(Ident fullname, List<Var> args) {
    this.fullname = fullname;
    this.args = args;
  }

  public List<Var> getArgs() {
    return args;
  }

  public Ident getFullname() {
    return fullname;
  }

  @Override
  public String toString() {
    return fullname.toString() + GenericListPrinter.paramsToStringWithBraces(args);
  }

}
