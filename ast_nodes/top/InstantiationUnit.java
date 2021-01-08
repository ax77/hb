package njast.ast_nodes.top;

import java.util.ArrayList;
import java.util.List;

import njast.ast_nodes.clazz.ClassDeclaration;

public class InstantiationUnit {
  private final List<ClassDeclaration> classes;

  public InstantiationUnit() {
    this.classes = new ArrayList<ClassDeclaration>();
  }

  public List<ClassDeclaration> getClasses() {
    return classes;
  }

  public void put(ClassDeclaration e) {
    this.classes.add(e);
  }

}
