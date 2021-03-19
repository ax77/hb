package ast_expr;

import java.io.Serializable;

import ast_class.ClassDeclaration;
import ast_vars.VarDeclarator;
import tokenize.Ident;

public class ExprIdent implements Serializable {
  private static final long serialVersionUID = 7777441284065170375L;

  private final Ident identifier;

  //MIR:TREE
  private ClassDeclaration staticClass;
  private VarDeclarator var;

  public ExprIdent(Ident identifier) {
    this.identifier = identifier;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public VarDeclarator getVar() {
    return var;
  }

  public void setVar(VarDeclarator var) {
    this.var = var;
  }

  public ClassDeclaration getStaticClass() {
    return staticClass;
  }

  public void setStaticClass(ClassDeclaration staticClass) {
    this.staticClass = staticClass;
  }

  public boolean isVar() {
    return var != null;
  }

  public boolean isStaticClass() {
    return staticClass != null;
  }

  @Override
  public String toString() {
    return identifier.getName();
  }

}
