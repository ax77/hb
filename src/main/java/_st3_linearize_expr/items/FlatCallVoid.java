package _st3_linearize_expr.items;

import java.util.List;

import _st3_linearize_expr.leaves.Var;
import ast_method.ClassMethodDeclaration;
import ast_printers.GenericListPrinter;
import ast_printers.TypePrinters;
import tokenize.Ident;

public class FlatCallVoid {
  private final ClassMethodDeclaration method;
  private final Ident function;
  private final List<Var> args;

  public FlatCallVoid(ClassMethodDeclaration method, Ident function, List<Var> args) {
    this.method = method;
    this.function = function;
    this.args = args;
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
