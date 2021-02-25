package _st4_linearize_stmt;

import ast_method.ClassMethodDeclaration;

public abstract class LinearBlockBuilder {

  public static LinearBlock build(ClassMethodDeclaration method) {
    RewriterStmt rw = new RewriterStmt(method);
    return rw.getResult();
  }

}
