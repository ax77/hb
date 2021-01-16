package njast.ast_nodes.stmt;

import java.io.Serializable;
import java.util.List;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.expr.ExprExpression;
import njast.ast_nodes.expr.ExprIdent;

public class StmtFor implements Serializable {
  private static final long serialVersionUID = 427234708626782894L;

  private final ExprExpression iter;
  private final ExprExpression collection;
  private final StmtStatement loop;

  // we'll rewrite short form to its full form
  // for item in collection {} 
  // ->
  // let iter: iterator<TYPE> = collection.get_iterator();
  // for(TYPE item = iter.current(); iter.has_next(); item = iter.get_next()) {}
  //
  private List<VarDeclarator> decl;
  private ExprExpression test;
  private ExprExpression step;

  public StmtFor(Ident iter, Ident collection, StmtStatement loop) {
    this.iter = new ExprExpression(new ExprIdent(iter));
    this.collection = new ExprExpression(new ExprIdent(collection));
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

  public List<VarDeclarator> getDecl() {
    return decl;
  }

  public void setDecl(List<VarDeclarator> decl) {
    this.decl = decl;
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

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("for(");

    if (decl != null) {
      for (VarDeclarator var : decl) {
        sb.append(var.toString());
      }
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

    sb.append("\n{\n");
    if (loop != null) {
      sb.append(loop.toString());
    }
    sb.append("\n}\n");

    return sb.toString();

    //    StringBuilder sb = new StringBuilder();
    //    sb.append("for ");
    //
    //    sb.append(iter.toString());
    //    sb.append(" in ");
    //    sb.append(collection.toString());
    //
    //    sb.append("\n{\n");
    //    if (loop != null) {
    //      sb.append(loop.toString());
    //    }
    //    sb.append("\n}\n");
    //
    //    return sb.toString();
  }

}
