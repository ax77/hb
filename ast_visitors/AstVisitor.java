package njast.ast_visitors;

import jscan.symtab.Ident;
import njast.ast_flow.expr.CExpression;
import njast.ast_flow.expr.FieldAccess;
import njast.ast_flow.expr.MethodInvocation;

public interface AstVisitor {

  void visit(CExpression o);

  void visit(MethodInvocation o);

  void visit(FieldAccess o);

  void visit(Ident o);

  String getText();

}
