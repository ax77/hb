package ast_unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_st1_templates.TypeSpecialUnhide;
import errors.AstParseException;
import hashed.Hash_ident;
import tokenize.Ident;

public class CompilationUnit {

  private final List<ClassDeclaration> classes;
  private final List<ClassDeclaration> templates;
  private final List<ClassDeclaration> forwards;

  public CompilationUnit() {
    this.classes = new ArrayList<>();
    this.templates = new ArrayList<>();
    this.forwards = new ArrayList<>();
  }

  public void putClazz(ClassDeclaration clazz) {
    this.classes.add(clazz);
    clazz.getTypeSetters().add(new TypeSpecialUnhide(clazz));
  }

  public void putTemplate(ClassDeclaration clazz) {
    this.templates.add(clazz);
    clazz.getTypeSetters().add(new TypeSpecialUnhide(clazz));
  }

  public void putForward(ClassDeclaration clazz) {
    this.forwards.add(clazz);
  }

  public List<ClassDeclaration> getClasses() {
    return classes;
  }

  public List<ClassDeclaration> getTemplates() {
    return templates;
  }

  public List<ClassDeclaration> getForwards() {
    return forwards;
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

  // not necessary, only for debug 
  public void sort() {
    Collections.sort(classes);
    Collections.sort(templates);
  }

}
