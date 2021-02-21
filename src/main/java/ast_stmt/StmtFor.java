package ast_stmt;

import java.io.Serializable;

import ast_expr.ExprExpression;
import ast_st3_tac.FlatCode;
import ast_vars.VarDeclarator;

public class StmtFor implements Serializable {
  private static final long serialVersionUID = 427234708626782894L;

  private /*final*/ VarDeclarator decl;
  private /*final*/ ExprExpression init;
  private /*final*/ ExprExpression test;
  private /*final*/ ExprExpression step;
  private /*final*/ StmtBlock block;

  /// 3ac
  private FlatCode linearDecl;
  private FlatCode linearInit;
  private FlatCode linearTest;
  private FlatCode linearStep;

  public StmtFor() {
  }

  public VarDeclarator getDecl() {
    return decl;
  }

  public ExprExpression getInit() {
    return init;
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

  public boolean hasDecl() {
    return decl != null;
  }

  public boolean hasInit() {
    return init != null;
  }

  public boolean hasTest() {
    return test != null;
  }

  public boolean hasStep() {
    return step != null;
  }

  public FlatCode getLinearDecl() {
    return linearDecl;
  }

  public void setLinearDecl(FlatCode linearDecl) {
    this.linearDecl = linearDecl;
  }

  public FlatCode getLinearInit() {
    return linearInit;
  }

  public void setLinearInit(FlatCode linearInit) {
    this.linearInit = linearInit;
  }

  public FlatCode getLinearTest() {
    return linearTest;
  }

  public void setLinearTest(FlatCode linearTest) {
    this.linearTest = linearTest;
  }

  public FlatCode getLinearStep() {
    return linearStep;
  }

  public void setLinearStep(FlatCode linearStep) {
    this.linearStep = linearStep;
  }

  public void setDecl(VarDeclarator decl) {
    this.decl = decl;
  }

  public void setInit(ExprExpression init) {
    this.init = init;
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
    sb.append("for(");

    if (decl != null) {
      sb.append(decl.toString());
    } else {
      sb.append("; "); // because we already have semicolon in VAR
    }

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
