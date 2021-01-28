package ast_expr;

import java.io.Serializable;

import ast_types.ArrayType;
import ast_vars.VarDeclarator;
import tokenize.Ident;

public class ExprFieldAccess implements Serializable {

  private static final long serialVersionUID = -6528124385251141959L;

  // object.fieldName
  // where object will be evaluated as expression at stage-2
  private final ExprExpression object;
  private final Ident fieldName;

  //MIR:TREE
  private VarDeclarator field;
  private ArrayType array; // array.length

  public ExprFieldAccess(ExprExpression object, Ident fieldName) {
    this.object = object;
    this.fieldName = fieldName;
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

  public ArrayType getArray() {
    return array;
  }

  public void setArray(ArrayType array) {
    this.array = array;
  }

  public boolean isArrayLengthExpression() {
    return array != null;
  }

  @Override
  public String toString() {
    return object.toString() + "." + fieldName.getName();
  }

}
