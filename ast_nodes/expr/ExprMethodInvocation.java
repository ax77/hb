package njast.ast_nodes.expr;

import java.util.List;

import jscan.symtab.Ident;
import njast.ast_utils.ExprUtil;

public class ExprMethodInvocation {
  private final ExprExpression object;
  private final List<ExprExpression> arguments;
  private final Ident funcname;
  private final boolean isMethodInvocation;

  // a.b()
  public ExprMethodInvocation(Ident funcname, ExprExpression object, List<ExprExpression> arguments) {
    this.funcname = funcname;
    this.object = object;
    this.arguments = arguments;
    this.isMethodInvocation = true;
  }

  // b()
  public ExprMethodInvocation(Ident funcname, List<ExprExpression> arguments) {
    this.funcname = funcname;
    this.object = null;
    this.arguments = arguments;
    this.isMethodInvocation = false;
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

  public boolean isMethodInvocation() {
    return isMethodInvocation;
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
    sb.append(ExprUtil.exprListCommaToString(arguments));
    sb.append(")");

    return sb.toString();
  }

}
