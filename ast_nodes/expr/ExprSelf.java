package njast.ast_nodes.expr;

import java.io.Serializable;

import njast.ast_nodes.clazz.ClassDeclaration;

public class ExprSelf implements Serializable {
  private static final long serialVersionUID = -3502102052762561251L;
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
