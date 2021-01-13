package njast.ast_nodes;

import java.io.Serializable;

import jscan.symtab.Ident;
import njast.ast_nodes.expr.ExprExpression;
import njast.parse.NullChecker;

public class FuncArg implements Serializable {
  private static final long serialVersionUID = 5950072201175054473L;

  private final Ident label; // f(x: 1)
  private final ExprExpression expression;

  public FuncArg(Ident label, ExprExpression expression) {
    NullChecker.check(label, expression);
    this.label = label;
    this.expression = expression;
  }

  public Ident getLabel() {
    return label;
  }

  public ExprExpression getExpression() {
    return expression;
  }

  @Override
  public String toString() {
    return label.getName() + ": " + expression.toString();
  }

}
