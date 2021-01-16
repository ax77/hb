package njast.ast_nodes.stmt;

import java.io.Serializable;

import jscan.symtab.Ident;
import njast.ast_nodes.expr.ExprExpression;
import njast.ast_nodes.expr.ExprPrimaryIdent;

public class StmtFor implements Serializable {
  private static final long serialVersionUID = 427234708626782894L;

  private final ExprExpression iter;
  private final ExprExpression collection;
  private final StmtStatement loop;

  public StmtFor(Ident iter, Ident collection, StmtStatement loop) {
    this.iter = new ExprExpression(new ExprPrimaryIdent(iter));
    this.collection = new ExprExpression(new ExprPrimaryIdent(collection));
    this.loop = loop;
  }

  public StmtStatement getLoop() {
    return loop;
  }

  public ExprExpression getIter() {
    return iter;
  }

  public ExprExpression getCollection() {
    return collection;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("for ");

    sb.append(iter.toString());
    sb.append(" in ");
    sb.append(collection.toString());

    sb.append("\n{\n");
    if (loop != null) {
      sb.append(loop.toString());
    }
    sb.append("\n}\n");

    return sb.toString();
  }

}
