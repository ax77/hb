package ast_unit;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import errors.AstParseException;
import hashed.Hash_ident;
import tokenize.Ident;

public class CompilationUnit {

  private final List<ClassDeclaration> classes;
  private final List<ClassDeclaration> templates;

  public CompilationUnit() {
    this.classes = new ArrayList<>();
    this.templates = new ArrayList<>();
  }

  public void putClazz(ClassDeclaration clazz) {
    if (alreadyContains(clazz, classes)) {
      throw new AstParseException("unexp.: " + clazz.getIdentifier().toString());
    }
    this.classes.add(clazz);
  }

  public void putTemplate(ClassDeclaration clazz) {
    if (alreadyContains(clazz, templates)) {
      throw new AstParseException("unexp.: " + clazz.getIdentifier().toString());
    }
    this.templates.add(clazz);
  }

  public List<ClassDeclaration> getClasses() {
    return classes;
  }

  public List<ClassDeclaration> getTemplates() {
    return templates;
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
