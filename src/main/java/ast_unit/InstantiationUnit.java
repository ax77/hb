package ast_unit;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;

public class InstantiationUnit {
  private final List<ClassDeclaration> classes;

  public InstantiationUnit() {
    this.classes = new ArrayList<>();
  }

  public List<ClassDeclaration> getClasses() {
    return classes;
  }

  public void put(ClassDeclaration e) {
    this.classes.add(0, e);
  }

}
