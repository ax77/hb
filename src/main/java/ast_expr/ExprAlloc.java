package ast_expr;

import ast_class.ClassDeclaration;
import ast_types.Type;

public class ExprAlloc {
  private final ClassDeclaration object;

  public ExprAlloc(ClassDeclaration object) {
    this.object = object;
  }

  public ClassDeclaration getObject() {
    return object;
  }

  @Override
  public String toString() {
    return "allocate(sizeof(" + object.getIdentifier() + "))";
  }

}
