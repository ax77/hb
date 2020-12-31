package njast.ast_nodes.clazz;

import njast.ast_nodes.clazz.vars.VarDeclarator;

public class ClassFieldDeclaration {

  private final ClassDeclaration owner;
  private final VarDeclarator field;

  public ClassFieldDeclaration(ClassDeclaration owner, VarDeclarator field) {
    this.owner = owner;
    this.field = field;
  }

  public VarDeclarator getField() {
    return field;
  }

  public ClassDeclaration getOwner() {
    return owner;
  }

  @Override
  public String toString() {
    return field.toString() + " owner=" + owner.toString();
  }

}
