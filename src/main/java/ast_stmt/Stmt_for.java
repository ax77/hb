package ast_stmt;

import java.io.Serializable;
import java.util.List;

import ast_expr.ExprExpression;
import ast_expr.ExprIdent;
import ast_vars.VarDeclarator;
import tokenize.Ident;
import tokenize.Token;

public class Stmt_for implements Serializable {
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
  private final StmtBlock loop;

  public Stmt_for(Ident auxIter, Ident auxCollection, StmtBlock loop, Token from) {
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
