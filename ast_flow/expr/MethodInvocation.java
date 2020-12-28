package njast.ast_flow.expr;

import java.util.List;

import jscan.symtab.Ident;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class MethodInvocation implements AstTraverser {
  private final CExpression function;
  private final List<CExpression> arguments;
  private final Ident funcname;

  public MethodInvocation(Ident funcname, CExpression function, List<CExpression> arguments) {
    this.funcname = funcname;
    this.function = function;
    this.arguments = arguments;
  }

  public CExpression getFunction() {
    return function;
  }

  public List<CExpression> getArguments() {
    return arguments;
  }

  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

  public Ident getFuncname() {
    return funcname;
  }

}
