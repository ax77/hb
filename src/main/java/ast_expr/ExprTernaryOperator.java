package ast_expr;

import java.io.Serializable;

import utils_oth.NullChecker;

public class ExprTernaryOperator implements Serializable {
  private static final long serialVersionUID = 3094072774409495573L;
  private final ExprExpression condition;
  private final ExprExpression trueResult;
  private final ExprExpression falseResult;

  public ExprTernaryOperator(ExprExpression condition, ExprExpression trueResult, ExprExpression falseResult) {
    NullChecker.check(condition, trueResult, falseResult);
    this.condition = condition;
    this.trueResult = trueResult;
    this.falseResult = falseResult;
  }

  public ExprExpression getCondition() {
    return condition;
  }

  public ExprExpression getTrueResult() {
    return trueResult;
  }

  public ExprExpression getFalseResult() {
    return falseResult;
  }

  private String toBraces(ExprExpression e) {
    return "(" + e.toString() + ")";
  }

  @Override
  public String toString() {
    return "(" + toBraces(condition) + " ? " + toBraces(trueResult) + " : " + toBraces(falseResult) + ")";
  }

}
