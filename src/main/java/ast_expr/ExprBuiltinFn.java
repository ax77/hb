package ast_expr;

import java.io.Serializable;
import java.util.List;

import ast_types.Type;
import ast_types.TypePrinters;
import tokenize.Ident;
import utils_oth.NullChecker;

public class ExprBuiltinFn implements Serializable {
  private static final long serialVersionUID = 166218239967767110L;

  private final Ident function;
  private final List<ExprExpression> arguments;
  private final List<Type> typeArguments;
  private /*final*/ Type returnType;

  public ExprBuiltinFn(Ident function, List<Type> typeArguments, List<ExprExpression> arguments, Type returnType) {
    NullChecker.check(function, arguments, returnType);

    this.function = function;
    this.arguments = arguments;
    this.typeArguments = typeArguments;
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

  public List<Type> getTypeArguments() {
    return typeArguments;
  }

  @Override
  public String toString() {
    return "builtin." + function.getName() + "_" + TypePrinters.typeArgumentsToString(typeArguments)
        + ExprUtil.funcArgsToString(arguments);
  }

}
