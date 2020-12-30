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

  private Symbol symMethod; // MARK:HIR
  private Symbol symObject; // MARK:HIR

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

  public Symbol getSymMethod() {
    return symMethod;
  }

  public void setSymMethod(Symbol symMethod) {
    this.symMethod = symMethod;
  }

  public Symbol getSymObject() {
    return symObject;
  }

  public void setSymObject(Symbol symObject) {
    this.symObject = symObject;
  }

  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

}
