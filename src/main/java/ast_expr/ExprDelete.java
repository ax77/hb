package ast_expr;

import java.io.Serializable;

import ast_vars.VarDeclarator;
import tokenize.Ident;

public class ExprDelete implements Serializable {
  private static final long serialVersionUID = -1415575456521181748L;

  private final Ident name;

  //MIR:
  private VarDeclarator var;

  public ExprDelete(Ident name) {
    this.name = name;
  }

  public Ident getName() {
    return name;
  }

  public VarDeclarator getVar() {
    return var;
  }

  public void setVar(VarDeclarator var) {
    this.var = var;
  }

  @Override
  public String toString() {
    return "delete(" + name + ")";
  }

}
