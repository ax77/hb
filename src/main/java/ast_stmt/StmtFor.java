package ast_stmt;

import java.io.Serializable;

import ast_expr.ExprExpression;

public class StmtFor implements Serializable {
  private static final long serialVersionUID = 427234708626782894L;

  private ExprExpression test;
  private ExprExpression step;
  private StmtBlock block;

  public StmtFor() {
  }

  public ExprExpression getTest() {
    return test;
  }

  public ExprExpression getStep() {
    return step;
  }

  public StmtBlock getBlock() {
    return block;
  }

  public boolean hasTest() {
    return test != null;
  }

  public boolean hasStep() {
    return step != null;
  }

  public void setTest(ExprExpression test) {
    this.test = test;
  }

  public void setStep(ExprExpression step) {
    this.step = step;
  }

  public void setBlock(StmtBlock block) {
    this.block = block;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("for(; ");

    if (test != null) {
      sb.append(test.toString());
    }
    sb.append("; ");

    if (step != null) {
      sb.append(step.toString());
    }
    sb.append(")");

    sb.append(block.toString());
    return sb.toString();
  }

}
