package njast.ast_nodes.expr;

import java.util.List;

import jscan.symtab.Ident;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;
import njast.ast_visitors.Symbol;

public class ExprMethodInvocation implements AstTraverser {
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
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

}
