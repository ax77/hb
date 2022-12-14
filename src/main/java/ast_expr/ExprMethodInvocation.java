package ast_expr;

import java.io.Serializable;
import java.util.List;

import ast_method.ClassMethodDeclaration;
import ast_printers.ArgsListToString;
import tokenize.Ident;
import utils_oth.NullChecker;

public class ExprMethodInvocation implements Serializable {
  private static final long serialVersionUID = -6288485017757935715L;

  // object.funcname(arguments)
  private final ExprExpression object;
  private final Ident funcname;
  private final List<ExprExpression> arguments;

  //MIR:TREE
  private ClassMethodDeclaration method;

  // a.b()
  // self.b()
  public ExprMethodInvocation(ExprExpression object, Ident funcname, List<ExprExpression> arguments) {
    NullChecker.check(funcname, object, arguments);

    this.funcname = funcname;
    this.object = object;
    this.arguments = arguments;
  }

  public ExprExpression getObject() {
    return object;
  }

  public List<ExprExpression> getArguments() {
    return arguments;
  }

  public Ident getFuncname() {
    return funcname;
  }

  public ClassMethodDeclaration getMethod() {
    return method;
  }

  public void setMethod(ClassMethodDeclaration method) {
    this.method = method;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(object.toString());
    sb.append(".");

    sb.append(funcname.getName());
    sb.append(ArgsListToString.paramsToStringWithBraces(arguments, '('));

    return sb.toString();
  }

}
