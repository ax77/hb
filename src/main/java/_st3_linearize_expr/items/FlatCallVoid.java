package _st3_linearize_expr.items;

import java.util.List;

import _st3_linearize_expr.leaves.Var;
import ast_printers.GenericListPrinter;

public class FlatCallVoid {

  private final String fullname;
  private final List<Var> args;

  public FlatCallVoid(String fullname, List<Var> args) {
    this.fullname = fullname;
    this.args = args;
  }

  public List<Var> getArgs() {
    return args;
  }

  public String getFullname() {
    return fullname;
  }

  @Override
  public String toString() {
    return fullname + GenericListPrinter.paramsToStringWithBraces(args);
  }

}
