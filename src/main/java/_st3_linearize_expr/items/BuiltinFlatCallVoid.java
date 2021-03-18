package _st3_linearize_expr.items;

import java.util.List;

import _st3_linearize_expr.leaves.Var;
import ast_printers.GenericListPrinter;
import tokenize.Ident;

public class BuiltinFlatCallVoid {

  private final Ident originalName;
  private final String fullname;
  private final List<Var> args;

  public BuiltinFlatCallVoid(Ident originalName, String fullname, List<Var> args) {
    this.originalName = originalName;
    this.fullname = fullname;
    this.args = args;
  }

  public List<Var> getArgs() {
    return args;
  }

  public String getFullname() {
    return fullname;
  }

  public Ident getOriginalName() {
    return originalName;
  }

  @Override
  public String toString() {
    return fullname + GenericListPrinter.paramsToStringWithBraces(args);
  }

}
