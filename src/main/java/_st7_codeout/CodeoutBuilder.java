package _st7_codeout;

import _st4_linearize_stmt.RewriterStmt;
import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_unit.InstantiationUnit;
import errors.AstParseException;

public abstract class CodeoutBuilder {

  public static Codeout build(final InstantiationUnit unit) {

    Codeout result = new Codeout();
    for (ClassDeclaration c : unit.getClasses()) {
      result.add(c);
      if (c.isInterface()) {
        continue;
      }
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

    for (ClassMethodDeclaration method : c.getTests()) {
      RewriterStmt rw = new RewriterStmt(method);
      result.add(new Function(method, rw.getResult()));
    }

    // TODO:static_semantic
    if (c.getDestructor() == null) {
      final boolean isOk = c.isStaticClass() || c.isMainClass();
      if (!isOk) {
        throw new AstParseException("something wrong. destructor is missing.");
      }
      return;
    }

    RewriterStmt rw = new RewriterStmt(c.getDestructor());
    result.add(new Function(c.getDestructor(), rw.getResult()));

  }

}
