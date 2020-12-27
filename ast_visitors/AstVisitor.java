package njast.ast_visitors;

import njast.ast_top.TypeDeclaration;
import njast.ast_top.CompilationUnit;

public interface AstVisitor {

  void visit(CompilationUnit o);

  void visit(TypeDeclaration o);

  String getText();

}
