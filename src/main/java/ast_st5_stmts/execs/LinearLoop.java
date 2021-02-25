package ast_st5_stmts.execs;

import ast_st3_tac.LinearExpression;

public class LinearLoop {
  /// these two only for debug, they are not usable
  private LinearExpression test;
  private LinearExpression step;

  private final LinearBlock block;

  public LinearLoop() {
    this.block = new LinearBlock();
  }

  public LinearExpression getTest() {
    return test;
  }

  public LinearExpression getStep() {
    return step;
  }

  public LinearBlock getBlock() {
    return block;
  }

  public void setTest(LinearExpression test) {
    this.test = test;
  }

  public void setStep(LinearExpression step) {
    this.step = step;
  }

  public boolean hasTest() {
    return test != null;
  }

  public boolean hasStep() {
    return step != null;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("for(;;)");
    sb.append(block.toString());
    return sb.toString();
  }

}
