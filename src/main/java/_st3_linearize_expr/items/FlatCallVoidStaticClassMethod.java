package _st3_linearize_expr.items;

import java.util.List;

import _st3_linearize_expr.leaves.Var;
import ast_method.ClassMethodDeclaration;
import ast_printers.GenericListPrinter;

public class FlatCallVoidStaticClassMethod {

  private final ClassMethodDeclaration method;
  private final String fullname;
  private final List<Var> args;

  public FlatCallVoidStaticClassMethod(ClassMethodDeclaration method, String fullname, List<Var> args) {
    this.method = method;
    this.fullname = fullname;
    this.args = args;
  }

  public List<Var> getArgs() {
    return args;
  }

  public String getFullname() {
    return fullname;
  }

  public ClassMethodDeclaration getMethod() {
    return method;
  }

  @Override
  public String toString() {
    return fullname + GenericListPrinter.paramsToStringWithBraces(args);
  }

}
