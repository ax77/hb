package ast.ast.nodes.expr;

import java.io.Serializable;

import ast.ast.nodes.vars.VarDeclarator;
import jscan.tokenize.Ident;

public class ExprIdent implements Serializable {
  private static final long serialVersionUID = 7777441284065170375L;
  private final Ident identifier;

  //MIR:TREE
  private VarDeclarator variable;

  public ExprIdent(Ident identifier) {
    this.identifier = identifier;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public VarDeclarator getVariable() {
    return variable;
  }

  public void setVariable(VarDeclarator variable) {
    this.variable = variable;
  }

  @Override
  public String toString() {
    return identifier.getName();
  }

}
