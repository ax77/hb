package njast.ast_nodes.top;

import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class TopLevelTypeDeclaration implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

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
