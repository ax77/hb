package _st7_codeout;

import _st4_linearize_stmt.RewriterStmt;
import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_unit.InstantiationUnit;

public abstract class CodeoutBuilder {

  public static Codeout build(final InstantiationUnit unit) {

    Codeout result = new Codeout();
    for (ClassDeclaration c : unit.getClasses()) {
      result.add(c);
      meth(c, result);
    }

    return result;
  }

  private static void meth(ClassDeclaration c, Codeout result) {
    for (ClassMethodDeclaration method : c.getConstructors()) {
      final RewriterStmt rw = new RewriterStmt(method);
      result.add(new Function(method, rw.getResult()));
    }
    for (ClassMethodDeclaration method : c.getMethods()) {
      RewriterStmt rw = new RewriterStmt(method);
      result.add(new Function(method, rw.getResult()));
    }
    RewriterStmt rw = new RewriterStmt(c.getDestructor());
    result.add(new Function(c.getDestructor(), rw.getResult()));
  }

}
