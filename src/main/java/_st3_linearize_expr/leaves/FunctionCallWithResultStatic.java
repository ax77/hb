package _st3_linearize_expr.leaves;

import java.util.List;

import ast_method.ClassMethodDeclaration;
import ast_printers.ArgsListToString;
import ast_types.Type;

public class FunctionCallWithResultStatic {
  private final ClassMethodDeclaration method;
  private final String fullname;
  private final Type type;
  private final List<Var> args;

  public FunctionCallWithResultStatic(ClassMethodDeclaration method, String fullname, Type type, List<Var> args) {
    this.method = method;
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

  public ClassMethodDeclaration getMethod() {
    return method;
  }

  @Override
  public String toString() {
    return fullname + ArgsListToString.paramsToStringWithBraces(args, '(');
  }

}
