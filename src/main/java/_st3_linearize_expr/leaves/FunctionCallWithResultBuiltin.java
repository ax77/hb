package _st3_linearize_expr.leaves;

import java.util.List;

import ast_printers.GenericListPrinter;
import ast_types.Type;
import tokenize.Ident;

public class FunctionCallWithResultBuiltin {
  private final Ident originalName;
  private final String fullname;
  private final Type type;
  private final List<Var> args;

  public FunctionCallWithResultBuiltin(Ident originalName, String fullname, Type type, List<Var> args) {
    this.originalName = originalName;
    this.fullname = fullname;
    this.type = type;
    this.args = args;
  }

  public Type getType() {
    return type;
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
