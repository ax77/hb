package ast_expr;

import java.io.Serializable;

import ast_vars.VarDeclarator;
import tokenize.Ident;

public class ExprFieldAccess implements Serializable {

  private static final long serialVersionUID = -6528124385251141959L;
  private final Ident fieldName;
  private final ExprExpression object;

  //MIR:TREE
  private VarDeclarator field;

  public ExprFieldAccess(Ident fieldName, ExprExpression object) {
    this.fieldName = fieldName;
    this.object = object;
  }

  public Ident getFieldName() {
    return fieldName;
  }

  public ExprExpression getObject() {
    return object;
  }

  public VarDeclarator getField() {
    return field;
  }

  public void setField(VarDeclarator field) {
    this.field = field;
  }

  @Override
  public String toString() {
    return object.toString() + "." + fieldName.getName();
  }

}