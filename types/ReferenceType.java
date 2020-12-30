package njast.types;

import njast.ast_nodes.clazz.ClassDeclaration;

public class ReferenceType {
  private final ClassDeclaration classType;

  public ReferenceType(ClassDeclaration classType) {
    this.classType = classType;
  }

  public ClassDeclaration getTypeName() {
    return classType;
  }

}
