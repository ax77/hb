package njast.ast_visitors;

import njast.ast_top.CompilationUnit;
import njast.ast_top.TypeDeclaration;

public interface AstVisitor {

  void visit(CompilationUnit o);

  void visit(TypeDeclaration o);

  String getText();

}
