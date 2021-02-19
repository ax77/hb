package ast_st3_tac.leaves;

import java.util.List;

import ast_printers.GenericListPrinter;
import ast_types.Type;
import tokenize.Ident;

public class PureFunctionCallWithResult {
  private final Type type;
  private final Ident function;
  private final List<Var> args;

  public PureFunctionCallWithResult(Type type, Ident function, List<Var> args) {
    this.type = type;
    this.function = function;
    this.args = args;
  }

  public Type getType() {
    return type;
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
