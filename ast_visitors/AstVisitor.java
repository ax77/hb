package njast.ast_visitors;

import jscan.symtab.Ident;
import njast.ast_nodes.expr.ExpressionNode;
import njast.ast_nodes.expr.FieldAccess;
import njast.ast_nodes.expr.MethodInvocation;

public interface AstVisitor {

  void visit(ExpressionNode o);

  void visit(MethodInvocation o);

  void visit(FieldAccess o);

  void visit(Ident o);

  String getText();

}
