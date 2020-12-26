package njast.ast_class;

import java.util.ArrayList;
import java.util.List;

import jscan.symtab.Ident;

public class ClassDeclaration {
  //<class body declaration> ::= <class member declaration> | <static initializer> | <constructor declaration>

  private final Ident identifier;

  // constructors
  private List<ConstructorDeclaration> constructors;

  // static-initializers
  // TODO:

  // members
  private List<FieldDeclaration> fields;
  private List<MethodDeclaration> methods;

  public ClassDeclaration(Ident identifier) {
    this.identifier = identifier;
    initLists();
  }

  private void initLists() {
    this.constructors = new ArrayList<ConstructorDeclaration>();
    this.fields = new ArrayList<FieldDeclaration>();
    this.methods = new ArrayList<MethodDeclaration>();
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public void put(ConstructorDeclaration e) {
    this.constructors.add(e);
  }

  public void put(FieldDeclaration e) {
    this.fields.add(e);
  }

  public void put(MethodDeclaration e) {
    this.methods.add(e);
  }

  public List<ConstructorDeclaration> getConstructorDeclaration() {
    return constructors;
  }

  public List<FieldDeclaration> getFieldDeclaration() {
    return fields;
  }

  public List<MethodDeclaration> getMethodDeclaration() {
    return methods;
  }

}
