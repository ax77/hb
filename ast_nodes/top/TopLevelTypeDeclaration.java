package njast.ast_nodes.top;

import njast.ast_nodes.clazz.ClassDeclaration;

public class TopLevelTypeDeclaration {

  private ClassDeclaration classDeclaration;

  public TopLevelTypeDeclaration() {
  }

  public TopLevelTypeDeclaration(ClassDeclaration classDeclaration) {
    this.classDeclaration = classDeclaration;
  }

  public ClassDeclaration getClassDeclaration() {
    return classDeclaration;
  }

  public void setClassDeclaration(ClassDeclaration classDeclaration) {
    this.classDeclaration = classDeclaration;
  }

}
