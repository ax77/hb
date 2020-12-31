package njast.ast_nodes.clazz;

import java.util.ArrayList;
import java.util.List;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.stmt.StmtBlock;

public class ClassDeclaration {

  private final Ident identifier;
  private List<ClassConstructorDeclaration> constructors;
  private List<StmtBlock> staticInitializers;
  private List<VarDeclarator> fields;
  private List<ClassMethodDeclaration> methods;

  public ClassDeclaration(Ident identifier) {
    this.identifier = identifier;
    initLists();
  }

  private void initLists() {
    this.constructors = new ArrayList<ClassConstructorDeclaration>();
    this.fields = new ArrayList<VarDeclarator>();
    this.methods = new ArrayList<ClassMethodDeclaration>();
    this.staticInitializers = new ArrayList<StmtBlock>();
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public void put(ClassConstructorDeclaration e) {
    this.constructors.add(e);
  }

  public void put(VarDeclarator e) {
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

  public List<VarDeclarator> getFields() {
    return fields;
  }

  public List<ClassMethodDeclaration> getMethods() {
    return methods;
  }

  public VarDeclarator getField(Ident name) {
    for (VarDeclarator field : fields) {
      if (field.getIdentifier().equals(name)) {
        return field;
      }
    }
    return null;
  }

  // TODO: overloading, of course
  public ClassMethodDeclaration getMethod(Ident name) {
    for (ClassMethodDeclaration method : methods) {
      if (method.getIdentifier().equals(name)) {
        return method;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return identifier.getName() + ": class";
  }

}
