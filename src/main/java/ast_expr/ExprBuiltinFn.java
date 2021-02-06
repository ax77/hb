package ast_expr;

import java.util.List;

import ast_types.Type;
import tokenize.Ident;
import utils_oth.NullChecker;

public class ExprBuiltinFn {
  private final Ident function;
  private final List<ExprExpression> arguments;
  private final Type returnType;

  public ExprBuiltinFn(Ident function, List<ExprExpression> arguments, Type returnType) {
    NullChecker.check(function, arguments, returnType);

    this.function = function;
    this.arguments = arguments;
    this.returnType = returnType;
  }

  public Ident getFunction() {
    return function;
  }

  public List<ExprExpression> getArguments() {
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
