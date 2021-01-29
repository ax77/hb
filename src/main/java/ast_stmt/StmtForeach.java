package ast_stmt;

import java.io.Serializable;

import ast_expr.ExprExpression;
import ast_expr.ExprIdent;
import tokenize.Ident;
import tokenize.Token;

public class StmtForeach implements Serializable {
  private static final long serialVersionUID = 427234708626782894L;

  private final ExprExpression auxIter;
  private final ExprExpression auxCollection;
  private final StmtBlock loop;

  public StmtForeach(Ident auxIter, Ident auxCollection, StmtBlock loop, Token from) {
    this.auxIter = new ExprExpression(new ExprIdent(auxIter), from);
    this.auxCollection = new ExprExpression(new ExprIdent(auxCollection), from);
    this.loop = loop;
  }

  public StmtBlock getLoop() {
    return loop;
  }

  public ExprExpression getAuxIter() {
    return auxIter;
  }

  public ExprExpression getAuxCollection() {
    return auxCollection;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("for ");

    sb.append(auxIter.toString());
    sb.append(" in ");
    sb.append(auxCollection.toString());

    if (loop != null) {
      sb.append(loop.toString());
    }

    return sb.toString();
  }

}
