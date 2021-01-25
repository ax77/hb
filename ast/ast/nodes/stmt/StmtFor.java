package ast.ast.nodes.stmt;

import java.io.Serializable;
import java.util.List;

import ast.ast.nodes.expr.ExprExpression;
import ast.ast.nodes.expr.ExprIdent;
import ast.ast.nodes.vars.VarDeclarator;
import jscan.tokenize.Ident;

public class StmtFor implements Serializable {
  private static final long serialVersionUID = 427234708626782894L;

  private final ExprExpression auxIter;
  private final ExprExpression auxCollection;

  // we'll rewrite short form to its full form
  // for item in collection {} 
  // ->
  // let iter: iterator<TYPE> = collection.get_iterator();
  // for(TYPE item = iter.current(); iter.has_next(); item = iter.get_next()) {}
  //
  private List<VarDeclarator> decl;
  private ExprExpression test;
  private ExprExpression step;
  private final StmtStatement loop;

  public StmtFor(Ident auxIter, Ident auxCollection, StmtStatement loop) {
    this.auxIter = new ExprExpression(new ExprIdent(auxIter));
    this.auxCollection = new ExprExpression(new ExprIdent(auxCollection));
    this.loop = loop;
  }

  public StmtStatement getLoop() {
    return loop;
  }

  public ExprExpression getAuxIter() {
    return auxIter;
  }

  public ExprExpression getAuxCollection() {
    return auxCollection;
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
