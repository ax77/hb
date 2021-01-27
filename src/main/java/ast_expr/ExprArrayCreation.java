package ast_expr;

import java.io.Serializable;

import ast_st1_templates.TypeSetter;
import ast_types.Type;
import ast_vars.VarDeclarator;

public class ExprArrayCreation implements Serializable, TypeSetter {
  private static final long serialVersionUID = 3782365086790137846L;

  private Type arrayType;

  //MIR:TREE
  private VarDeclarator var;

  public ExprArrayCreation(Type arrayType) {
    this.arrayType = arrayType;
  }

  @Override
  public void setType(Type typeToSet) {
    this.arrayType = typeToSet;
  }

  @Override
  public Type getType() {
    return arrayType;
  }

  public VarDeclarator getVar() {
    return var;
  }

  public void setVar(VarDeclarator var) {
    this.var = var;
  }

  @Override
  public String toString() {
    return "new " + arrayType.toString();
  }

}
