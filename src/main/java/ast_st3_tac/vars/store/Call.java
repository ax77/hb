package ast_st3_tac.vars.store;

import java.util.List;

import ast_printers.GenericListPrinter;
import ast_types.Type;
import tokenize.Ident;

public class Call {
  private final Type type;
  private final Ident function;
  private final List<Var> args;

  /// for rewriting
  private final boolean isConstructor;

  public Call(Type type, Ident function, List<Var> args, boolean isConstructor) {
    this.type = type;
    this.function = function;
    this.args = args;
    this.isConstructor = isConstructor;
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

  public boolean isConstructor() {
    return isConstructor;
  }

  @Override
  public String toString() {
    return function.toString() + GenericListPrinter.paramsToStringWithBraces(args);
  }

}
