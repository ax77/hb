package _st4_linearize_stmt.items;

import _st3_linearize_expr.leaves.Var;
import _st4_linearize_stmt.LinearBlock;

public class LinearSelection {
  private final Var condition;
  private final LinearBlock trueBlock;
  private LinearBlock elseBlock;

  public LinearSelection(Var condition) {
    this.condition = condition;
    this.trueBlock = new LinearBlock();
  }

  public LinearBlock getTrueBlock() {
    return trueBlock;
  }

  public LinearBlock getElseBlock() {
    return elseBlock;
  }

  public Var getCondition() {
    return condition;
  }

  public void setElseBlock(LinearBlock elseBlock) {
    this.elseBlock = elseBlock;
  }

  public boolean hasElse() {
    return elseBlock != null;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("if(");
    sb.append(condition.toString());
    sb.append(")");

    sb.append(trueBlock.toString());

    if (elseBlock != null) {
      sb.append("else ");
      sb.append(elseBlock.toString());
    }

    return sb.toString();
  }

}
