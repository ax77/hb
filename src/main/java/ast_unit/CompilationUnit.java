package ast_unit;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;

public class CompilationUnit {

  private final List<ClassDeclaration> classes;
  private final List<ClassDeclaration> templates;

  public CompilationUnit() {
    this.classes = new ArrayList<>();
    this.templates = new ArrayList<>();
  }

  public void putClazz(ClassDeclaration typeDeclaration) {
    this.classes.add(typeDeclaration);
  }

  public void putTemplate(ClassDeclaration typeDeclaration) {
    this.templates.add(typeDeclaration);
  }

  public List<ClassDeclaration> getClasses() {
    return classes;
  }

  public List<ClassDeclaration> getTemplates() {
    return templates;
  }

}
