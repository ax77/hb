package njast.ast_nodes.expr;

import njast.ast_nodes.clazz.ClassDeclaration;

public class ExprSelf {
  private final ClassDeclaration clazz;

  public ExprSelf(ClassDeclaration clazz) {
    this.clazz = clazz;
  }

  public ClassDeclaration getClazz() {
    return clazz;
  }

  @Override
  public String toString() {
    return "self";
  }

}
