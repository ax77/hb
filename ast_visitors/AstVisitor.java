package njast.ast_visitors;

import njast.ast_top.TypeDeclaration;
import njast.ast_top.TypeDeclarationsList;

public interface AstVisitor {

  void visit(TypeDeclarationsList o);

  void visit(TypeDeclaration o);

  String getText();

}
