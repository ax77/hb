package njast.ast_nodes.expr;

import jscan.tokenize.Token;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class ExprUnary implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

  private final Token operator;
  private final ExprExpression operand;

  public ExprUnary(Token operator, ExprExpression operand) {
    this.operator = operator;
    this.operand = operand;
  }

  public Token getOperator() {
    return operator;
  }

  public ExprExpression getOperand() {
    return operand;
  }

}
