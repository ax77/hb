package njast.ast_class;

import jscan.symtab.Ident;

public class ClassDeclaration {
  private Ident identifier; // njast:mark - symbol instead ident?
  private ClassBody classBody;

  public ClassDeclaration() {
  }

  public ClassDeclaration(Ident identifier, ClassBody classBody) {
    this.identifier = identifier;
    this.classBody = classBody;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public void setIdentifier(Ident identifier) {
    this.identifier = identifier;
  }

  public ClassBody getClassBody() {
    return classBody;
  }

  public void setClassBody(ClassBody classBody) {
    this.classBody = classBody;
  }

}
