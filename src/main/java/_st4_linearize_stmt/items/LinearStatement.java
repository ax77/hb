package _st4_linearize_stmt.items;

import _st3_linearize_expr.LinearExpression;
import _st4_linearize_stmt.LinearBlock;
import ast_stmt.StatementBase;

public class LinearStatement {

  private final StatementBase base;
  private LinearBlock linearBlock;
  private LinearLoop linearLoop;
  private LinearSelection linearSelection;
  private LinearExpression linearExpression;
  private LinearBreak linearBreak;
  private LinearContinue linearContinue;
  private LinearReturn linearReturn;

  public LinearStatement(LinearSelection linearSelection) {
    this.base = StatementBase.SIF;
    this.linearSelection = linearSelection;
  }

  public LinearStatement(LinearBlock linearBlock) {
    this.base = StatementBase.SBLOCK;
    this.linearBlock = linearBlock;
  }

  public LinearStatement(StatementBase base, LinearExpression linearExpression) {
    this.base = base;
    this.linearExpression = linearExpression;
  }

  public LinearStatement(LinearBreak linearBreak) {
    this.base = StatementBase.SBREAK;
    this.linearBreak = linearBreak;
  }

  public LinearStatement(LinearLoop linearLoop) {
    this.base = StatementBase.SFOR;
    this.linearLoop = linearLoop;
  }

  public LinearStatement(LinearContinue linearContinue) {
    this.base = StatementBase.SCONTINUE;
    this.linearContinue = linearContinue;
  }

  public LinearStatement(LinearReturn linearReturn) {
    this.base = StatementBase.SRETURN;
    this.linearReturn = linearReturn;
  }

  public StatementBase getBase() {
    return base;
  }

  public LinearBlock getLinearBlock() {
    return linearBlock;
  }

  public LinearLoop getLinearLoop() {
    return linearLoop;
  }

  public LinearSelection getLinearSelection() {
    return linearSelection;
  }

  public LinearExpression getLinearExpression() {
    return linearExpression;
  }

  public LinearBreak getLinearBreak() {
    return linearBreak;
  }

  public LinearContinue getLinearContinue() {
    return linearContinue;
  }

  public LinearReturn getLinearReturn() {
    return linearReturn;
  }

  @Override
  public String toString() {
    if (base == StatementBase.SIF) {
      return linearSelection.toString();
    }
    if (base == StatementBase.SEXPR) {
      return linearExpression.toString();
    }
    if (base == StatementBase.SBLOCK) {
      return linearBlock.toString();
    }
    if (base == StatementBase.SRETURN) {
      return linearReturn.toString();
    }
    if (base == StatementBase.SFOR) {
      return linearLoop.toString();
    }
    if (base == StatementBase.SBREAK) {
      return linearBreak.toString();
    }
    if (base == StatementBase.SCONTINUE) {
      return linearContinue.toString();
    }
    if (base == StatementBase.SVAR_DECLARATION) {
      return linearExpression.toString();
    }
    return base.toString();
  }
}
