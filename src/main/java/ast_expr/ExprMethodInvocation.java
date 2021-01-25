package ast_expr;

import java.util.List;

import ast_method.ClassMethodDeclaration;
import tokenize.Ident;
import utils_oth.NullChecker;

public class ExprMethodInvocation {
  private /*final*/ ExprExpression object;
  private final List<FuncArg> arguments;
  private final Ident funcname;

  //MIR:TREE
  private ClassMethodDeclaration method;

  // a.b()
  // self.b()
  public ExprMethodInvocation(Ident funcname, ExprExpression object, List<FuncArg> arguments) {
    NullChecker.check(funcname, object, arguments);

    this.funcname = funcname;
    this.object = object;
    this.arguments = arguments;
  }

  public ExprExpression getObject() {
    return object;
  }

  public List<FuncArg> getArguments() {
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
    sb.append("(");
    sb.append(ExprUtil.exprListCommaToString1(arguments));
    sb.append(")");

    return sb.toString();
  }

  public void setObject(ExprExpression exprExpression) {
    this.object = exprExpression;
  }

}
