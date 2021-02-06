package ast_stmt;

import java.io.Serializable;

import ast_expr.ExprExpression;
import ast_vars.VarDeclarator;

public class StmtFor implements Serializable {
  private static final long serialVersionUID = 427234708626782894L;

  private VarDeclarator decl;
  private ExprExpression init;
  private ExprExpression test;
  private ExprExpression step;
  private StmtStatement loop;

  public StmtFor(VarDeclarator decl, ExprExpression init, ExprExpression test, ExprExpression step,
      StmtStatement loop) {
    this.decl = decl;
    this.init = init;
    this.test = test;
    this.step = step;
    this.loop = loop;
  }

  public VarDeclarator getDecl() {
    return decl;
  }

  public void setDecl(VarDeclarator decl) {
    this.decl = decl;
  }

  public ExprExpression getInit() {
    return init;
  }

  public void setInit(ExprExpression init) {
    this.init = init;
  }

  public ExprExpression getTest() {
    return test;
  }

  public void setTest(ExprExpression test) {
    this.test = test;
  }

  public ExprExpression getStep() {
    return step;
  }

  public void setStep(ExprExpression step) {
    this.step = step;
  }

  public StmtStatement getLoop() {
    return loop;
  }

  public void setLoop(StmtStatement loop) {
    this.loop = loop;
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

    if (loop != null) {
      sb.append(loop.toString());
    }

    return sb.toString();
  }

}
