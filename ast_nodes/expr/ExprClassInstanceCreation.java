package njast.ast_nodes.expr;

import java.util.List;

import jscan.symtab.Ident;
import njast.ast_utils.ExprUtil;

public class ExprClassInstanceCreation {
  // <class instance creation expression> ::= new <class type> ( <argument list>? )

  private final Ident classtype;
  private final List<ExprExpression> arguments;

  public ExprClassInstanceCreation(Ident classtype, List<ExprExpression> arguments) {
    this.classtype = classtype;
    this.arguments = arguments;
  }

  public Ident getClasstype() {
    return classtype;
  }

  public List<ExprExpression> getArguments() {
    return arguments;
  }

  @Override
  public String toString() {
    return "new " + classtype.getName() + "(" + ExprUtil.exprListCommaToString(arguments) + ")";
  }

}
