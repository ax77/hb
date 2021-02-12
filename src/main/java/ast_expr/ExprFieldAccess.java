package ast_expr;

import java.io.Serializable;

import ast_st2_annotate.Symbol;
import tokenize.Ident;

public class ExprFieldAccess implements Serializable, MirSymbol {

  private static final long serialVersionUID = -6528124385251141959L;

  // object.fieldName
  // where object will be evaluated as expression at stage-2
  private final ExprExpression object;
  private final Ident fieldName;

  //MIR:TREE
  private Symbol sym;

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

  @Override
  public Symbol getSym() {
    return sym;
  }

  @Override
  public void setSym(Symbol sym) {
    this.sym = sym;
  }

  @Override
  public String toString() {
    return object.toString() + "." + fieldName.getName();
  }

}
