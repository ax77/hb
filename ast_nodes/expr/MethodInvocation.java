package njast.ast_nodes.expr;

import java.util.List;

import jscan.symtab.Ident;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class MethodInvocation implements AstTraverser {
  private final ExpressionNode function;
  private final List<ExpressionNode> arguments;
  private final Ident funcname;
  private final boolean isMethodInvocation;

  // a.b()
  public MethodInvocation(Ident funcname, ExpressionNode function, List<ExpressionNode> arguments) {
    this.funcname = funcname;
    this.function = function;
    this.arguments = arguments;
    this.isMethodInvocation = true;
  }

  // b()
  public MethodInvocation(Ident funcname, List<ExpressionNode> arguments) {
    this.funcname = funcname;
    this.function = null;
    this.arguments = arguments;
    this.isMethodInvocation = false;
  }

  public ExpressionNode getFunction() {
    return function;
  }

  public List<ExpressionNode> getArguments() {
    return arguments;
  }

  public Ident getFuncname() {
    return funcname;
  }

  public boolean isMethodInvocation() {
    return isMethodInvocation;
  }

  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

}
