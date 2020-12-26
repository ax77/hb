package njast.ast_class;

import njast.ast_class.fields.FieldDeclaration;
import njast.ast_class.methods.MethodDeclaration;

public class ClassMemberDeclaration {

  // <class member declaration> ::= <field declaration> | <method declaration>

  private FieldDeclaration fieldDeclaration;
  private MethodDeclaration methodDeclaration;
  private boolean isField;

  public ClassMemberDeclaration() {
  }

  public ClassMemberDeclaration(FieldDeclaration fieldDeclaration) {
    this.fieldDeclaration = fieldDeclaration;
    this.isField = true;
  }

  public ClassMemberDeclaration(MethodDeclaration methodDeclaration) {
    this.methodDeclaration = methodDeclaration;
    this.isField = false;
  }

  public FieldDeclaration getFieldDeclaration() {
    return fieldDeclaration;
  }

  public void setFieldDeclaration(FieldDeclaration fieldDeclaration) {
    this.fieldDeclaration = fieldDeclaration;
  }

  public MethodDeclaration getMethodDeclaration() {
    return methodDeclaration;
  }

  public void setMethodDeclaration(MethodDeclaration methodDeclaration) {
    this.methodDeclaration = methodDeclaration;
  }

  public boolean isField() {
    return isField;
  }

  public void setField(boolean isField) {
    this.isField = isField;
  }

}
