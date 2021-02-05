package ast_expr;

import java.util.List;

import ast_types.Type;
import tokenize.Ident;
import utils_oth.NullChecker;

public class ExprBuiltinFn {
  private final Ident function;
  private final List<FuncArg> arguments;
  private final Type returnType;

  public ExprBuiltinFn(Ident function, List<FuncArg> arguments, Type returnType) {
    NullChecker.check(function, arguments, returnType);

    this.function = function;
    this.arguments = arguments;
    this.returnType = returnType;
  }

  public Ident getFunction() {
    return function;
  }

  public List<FuncArg> getArguments() {
    return arguments;
  }

  public Type getReturnType() {
    return returnType;
  }

  @Override
  public String toString() {
    return "builtin." + function.getName() + ExprUtil.funcArgsToString(arguments);
  }

}
