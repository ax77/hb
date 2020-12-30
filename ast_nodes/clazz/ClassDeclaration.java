package njast.ast_nodes.clazz;

import java.util.ArrayList;
import java.util.List;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class ClassDeclaration implements AstTraverser {

  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }
  //<class body declaration> ::= <class member declaration> | <static initializer> | <constructor declaration>

  private final Ident identifier;
  private List<ClassConstructorDeclaration> constructors;
  private List<StmtBlock> staticInitializers;
  private List<ClassFieldDeclaration> fields;
  private List<ClassMethodDeclaration> methods;

  public ClassDeclaration(Ident identifier) {
    this.identifier = identifier;
    initLists();
  }

  private void initLists() {
    this.constructors = new ArrayList<ClassConstructorDeclaration>();
    this.fields = new ArrayList<ClassFieldDeclaration>();
    this.methods = new ArrayList<ClassMethodDeclaration>();
    this.staticInitializers = new ArrayList<StmtBlock>();
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public void put(ClassConstructorDeclaration e) {
    this.constructors.add(e);
  }

  public void put(ClassFieldDeclaration e) {
    this.fields.add(e);
  }

  public void put(ClassMethodDeclaration e) {
    this.methods.add(e);
  }

  public void put(StmtBlock e) {
    this.staticInitializers.add(e);
  }

  public List<ClassConstructorDeclaration> getConstructors() {
    return constructors;
  }

  public List<ClassFieldDeclaration> getFields() {
    return fields;
  }

  public List<ClassMethodDeclaration> getMethods() {
    return methods;
  }

}
