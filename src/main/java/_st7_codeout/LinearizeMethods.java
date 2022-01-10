package _st7_codeout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import _st4_linearize_stmt.RewriterStmt;
import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import errors.AstParseException;

public abstract class LinearizeMethods {

  public static List<Function> flat(ClassDeclaration c) {
    List<Function> result = new ArrayList<Function>();

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
      final boolean isOk = c.isStaticClass() || c.isEnum();
      if (!isOk) {
        throw new AstParseException("something wrong. destructor is missing.");
      }
    }

    else {
      RewriterStmt rw = new RewriterStmt(c.getDestructor());
      result.add(new Function(c.getDestructor(), rw.getResult()));
    }

    Collections.sort(result);
    return result;
  }

}
