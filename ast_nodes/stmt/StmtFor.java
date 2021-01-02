package njast.ast_nodes.stmt;

import java.io.Serializable;
import java.util.List;

import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.expr.ExprExpression;

public class StmtFor implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 427234708626782894L;
  // for
  private List<VarDeclarator> decl;
  private ExprExpression init;
  private ExprExpression test;
  private ExprExpression step;
  private StmtStatement loop;

  public StmtFor(List<VarDeclarator> decl, ExprExpression init, ExprExpression test, ExprExpression step,
      StmtStatement loop) {
    this.decl = decl;
    this.init = init;
    this.test = test;
    this.step = step;
    this.loop = loop;
  }

  public List<VarDeclarator> getDecl() {
    return decl;
  }

  public void setDecl(List<VarDeclarator> decl) {
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

}
