package _st3_linearize_expr.items;

import java.util.List;

import _st3_linearize_expr.leaves.Var;
import ast_method.ClassMethodDeclaration;
import ast_printers.GenericListPrinter;
import ast_printers.TypePrinters;
import errors.AstParseException;
import tokenize.Ident;
import utils_oth.NullChecker;

public class FlatCallConstructor {
  private final ClassMethodDeclaration method;
  private final Ident function;
  private final Var thisVar; // must be also the first arg into args 
  private final List<Var> args;

  public FlatCallConstructor(ClassMethodDeclaration method, Ident function, List<Var> args, Var thisVar) {
    NullChecker.check(function, args, thisVar);

    if (args.isEmpty() || !args.get(0).equals(thisVar)) {
      throw new AstParseException("constructor arguments are not correct");
    }

    this.method = method;
    this.function = function;
    this.args = args;
    this.thisVar = thisVar;
  }

  public Ident getFunction() {
    return function;
  }

  public List<Var> getArgs() {
    return args;
  }

  public Var getThisVar() {
    return thisVar;
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
