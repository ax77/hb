package njast.ast_nodes.stmt;

import java.io.Serializable;

import jscan.tokenize.Token;
import njast.ast_kinds.StatementBase;
import njast.ast_nodes.expr.ExprExpression;

public class StmtStatement implements Serializable {
  private static final long serialVersionUID = 2946438995245230886L;

  private final StatementBase base;
  private StmtBlock compound;
  private ExprExpression expr; // return expr or expr-stmt
  private StmtFor sfor;
  private Stmt_if sif;

  public StmtStatement(StmtFor sfor) {
    this.base = StatementBase.SFOR;
    this.sfor = sfor;
  }

  public StmtStatement(Stmt_if sif) {
    this.base = StatementBase.SIF;
    this.sif = sif;
  }

  // return <expr> ;
  // <expr> 
  public StmtStatement(StatementBase base, ExprExpression expr) {
    this.base = base;
    this.expr = expr;
  }

  // {  }
  public StmtStatement(Token from, StmtBlock compound) {
    this.base = StatementBase.SBLOCK;
    this.compound = compound;
  }

  public StmtBlock getCompound() {
    return compound;
  }

  public void setCompound(StmtBlock compound) {
    this.compound = compound;
  }

  public StatementBase getBase() {
    return base;
  }

  public ExprExpression getSexpression() {
    return expr;
  }

  public StmtFor getSfor() {
    return sfor;
  }

  public Stmt_if getSif() {
    return sif;
  }

  @Override
  public String toString() {
    if (base == StatementBase.SIF) {
      return sif.toString();
    }
    if (base == StatementBase.SEXPR) {
      return expr.toString() + ";\n";
    }
    if (base == StatementBase.SBLOCK) {
      return compound.toString();
    }
    if (base == StatementBase.SRETURN) {
      if (expr != null) {
        return "return " + expr.toString() + ";\n";
      } else {
        return "return;\n";
      }
    }
    return base.toString();
  }

}
