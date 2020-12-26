package njast.ast_class;

public class ClassBodyDeclaration {

  //  <class member declaration> | <static initializer> | <constructor declaration>

  private ClassMemberDeclaration classMemberDeclaration;

  public ClassBodyDeclaration() {
  }

  public ClassBodyDeclaration(ClassMemberDeclaration classMemberDeclaration) {
    this.classMemberDeclaration = classMemberDeclaration;
  }

  public ClassMemberDeclaration getClassMemberDeclaration() {
    return classMemberDeclaration;
  }

  public void setClassMemberDeclaration(ClassMemberDeclaration classMemberDeclaration) {
    this.classMemberDeclaration = classMemberDeclaration;
  }

}
