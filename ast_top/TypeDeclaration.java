package njast.ast_top;

import njast.ast_class.ClassDeclaration;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

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
