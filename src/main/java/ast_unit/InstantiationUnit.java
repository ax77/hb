package ast_unit;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import errors.AstParseException;
import hashed.Hash_ident;
import tokenize.Ident;

public class InstantiationUnit {
  private final List<ClassDeclaration> classes;

  public InstantiationUnit() {
    this.classes = new ArrayList<>();
  }

  public List<ClassDeclaration> getClasses() {
    return classes;
  }

  public void put(ClassDeclaration e) {
    checkRedefinition(e, classes);
    this.classes.add(0, e);
  }

  private void checkRedefinition(ClassDeclaration clazz, List<ClassDeclaration> where) {
    for (ClassDeclaration c : where) {
      if (c.getIdentifier().equals(clazz.getIdentifier())) {
        throw new AstParseException("the class is redefined: " + c.getIdentifier().toString());
      }
    }
  }

  // for unit-tests
  public ClassDeclaration getClassByName(String name) {
    Ident id = Hash_ident.getHashedIdent(name);
    for (ClassDeclaration c : classes) {
      if (c.getIdentifier().equals(id)) {
        return c;
      }
    }
    throw new AstParseException("has no class with name: " + name);
  }

}
