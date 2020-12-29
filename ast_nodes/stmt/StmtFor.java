package njast.ast_nodes.stmt;

import njast.ast_nodes.clazz.vars.VarDeclarationLocal;
import njast.ast_nodes.expr.ExprExpression;

public class StmtFor {

  // for
  private VarDeclarationLocal decl;
  private ExprExpression init;
  private ExprExpression test;
  private ExprExpression step;
  private StmtStatement loop;

  public StmtFor(VarDeclarationLocal decl, ExprExpression init, ExprExpression test, ExprExpression step,
      StmtStatement loop) {
    this.decl = decl;
    this.init = init;
    this.test = test;
    this.step = step;
    this.loop = loop;
  }

  public VarDeclarationLocal getDecl() {
    return decl;
  }

  public void setDecl(VarDeclarationLocal decl) {
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
