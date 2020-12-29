package njast.ast_nodes.expr;

import java.util.List;

import jscan.symtab.Ident;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class MethodInvocation implements AstTraverser {
  private final Expression function;
  private final List<Expression> arguments;
  private final Ident funcname;
  private final boolean isMethodInvocation;

  // a.b()
  public MethodInvocation(Ident funcname, Expression function, List<Expression> arguments) {
    this.funcname = funcname;
    this.function = function;
    this.arguments = arguments;
    this.isMethodInvocation = true;
  }

  // b()
  public MethodInvocation(Ident funcname, List<Expression> arguments) {
    this.funcname = funcname;
    this.function = null;
    this.arguments = arguments;
    this.isMethodInvocation = false;
  }

  public Expression getFunction() {
    return function;
  }

  public List<Expression> getArguments() {
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
