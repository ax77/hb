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
    this.classes.add(clazz);
  }

  public void putTemplate(ClassDeclaration clazz) {
    this.templates.add(clazz);
  }

  public List<ClassDeclaration> getClasses() {
    return classes;
  }

  public List<ClassDeclaration> getTemplates() {
    return templates;
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
