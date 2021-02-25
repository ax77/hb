package _st3_linearize_expr.leaves;

public class Ternary {
  private final Var condition;
  private final Var trueResult;
  private final Var falseResult;

  public Ternary(Var condition, Var trueResult, Var falseResult) {
    this.condition = condition;
    this.trueResult = trueResult;
    this.falseResult = falseResult;
  }

  public Var getCondition() {
    return condition;
  }

  public Var getTrueResult() {
    return trueResult;
  }

  public Var getFalseResult() {
    return falseResult;
  }

  @Override
  public String toString() {
    return "?(" + condition.toString() + ", " + trueResult.toString() + ", " + falseResult.toString() + ")";
  }

}
