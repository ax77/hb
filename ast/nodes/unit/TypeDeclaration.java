package njast.ast.nodes.unit;

import njast.ast.nodes.ClassDeclaration;

public class TypeDeclaration {

  private ClassDeclaration classDeclaration;

  public TypeDeclaration() {
  }

  public TypeDeclaration(ClassDeclaration classDeclaration) {
    this.classDeclaration = classDeclaration;
  }

  public ClassDeclaration getClassDeclaration() {
    return classDeclaration;
  }

  public void setClassDeclaration(ClassDeclaration classDeclaration) {
    this.classDeclaration = classDeclaration;
  }

}