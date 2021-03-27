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
    if (alreadyContains(e, classes)) {
      //return;
    }
    this.classes.add(0, e);
  }

  /// this means: we already parsed that class, but it was imported
  /// many times in a diffferent files, and we shouldn't define it
  /// again and again. actually, this is fragile solution, and
  /// it should be re-thinked...
  private boolean alreadyContains(ClassDeclaration clazz, List<ClassDeclaration> where) {
    for (ClassDeclaration c : where) {
      if (c.getIdentifier().equals(clazz.getIdentifier())) {
        return true;
      }
    }
    return false;
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
