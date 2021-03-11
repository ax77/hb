package _st3_linearize_expr.leaves;

import java.util.List;

import ast_method.ClassMethodDeclaration;
import ast_printers.GenericListPrinter;
import ast_printers.TypePrinters;
import ast_types.Type;
import tokenize.Ident;

public class PureFunctionCallWithResult {
  private final ClassMethodDeclaration method;
  private final Type type;
  private final Ident function;
  private final List<Var> args;

  public PureFunctionCallWithResult(ClassMethodDeclaration method, Type type, Ident function, List<Var> args) {
    this.method = method;
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

  public ClassMethodDeclaration getMethod() {
    return method;
  }

  @Override
  public String toString() {
    return function.toString() + TypePrinters.typeArgumentsToString(method.getClazz().getTypeParametersT())
        + GenericListPrinter.paramsToStringWithBraces(args);
  }

}
