package ast.ast.nodes.expr;

import java.util.List;

import ast.ast.nodes.method.ClassMethodDeclaration;
import ast.ast.utils.ExprUtil;
import jscan.tokenize.Ident;

public class ExprMethodInvocation {
  private /*final*/ ExprExpression object;
  private final List<FuncArg> arguments;
  private final Ident funcname;
  private final boolean isMethodInvocation;

  //MIR:TREE
  private ClassMethodDeclaration method;

  // a.b()
  public ExprMethodInvocation(Ident funcname, ExprExpression object, List<FuncArg> arguments) {
    this.funcname = funcname;
    this.object = object;
    this.arguments = arguments;
    this.isMethodInvocation = true;
  }

  // b()
  public ExprMethodInvocation(Ident funcname, List<FuncArg> arguments) {
    this.funcname = funcname;
    this.object = null;
    this.arguments = arguments;
    this.isMethodInvocation = false;
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

  public boolean isMethodInvocation() {
    return isMethodInvocation;
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

    if (isMethodInvocation) {
      sb.append(object.toString());
      sb.append(".");
    }

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
