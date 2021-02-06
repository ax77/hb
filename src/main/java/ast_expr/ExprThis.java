package ast_expr;

import java.io.Serializable;

import ast_class.ClassDeclaration;

public class ExprThis implements Serializable {
  private static final long serialVersionUID = -3502102052762561251L;
  private final ClassDeclaration clazz;

  public ExprThis(ClassDeclaration clazz) {
    this.clazz = clazz;
  }

  public ClassDeclaration getClazz() {
    return clazz;
  }

  @Override
  public String toString() {
    return "this";
  }

}
