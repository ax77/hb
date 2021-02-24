package ast_expr;

import utils_oth.NullChecker;

public class ExprTernaryOperator {
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
