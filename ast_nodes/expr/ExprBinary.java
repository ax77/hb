package njast.ast_nodes.expr;

import jscan.tokenize.Token;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class ExprBinary implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

  private final Token operator;
  private final ExprExpression lhs;
  private final ExprExpression rhs;

  public ExprBinary(Token operator, ExprExpression lhs, ExprExpression rhs) {
    this.operator = operator;
    this.lhs = lhs;
    this.rhs = rhs;
  }

  public Token getOperator() {
    return operator;
  }

  public ExprExpression getLhs() {
    return lhs;
  }

  public ExprExpression getRhs() {
    return rhs;
  }

  @Override
  public String toString() {
    return lhs.toString() + " " + operator.getValue() + " " + rhs.toString();
  }

}
