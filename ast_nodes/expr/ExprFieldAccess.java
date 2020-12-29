package njast.ast_nodes.expr;

import jscan.symtab.Ident;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class ExprFieldAccess implements AstTraverser {
  private final Ident fieldName;
  private final ExprExpression object;

  public ExprFieldAccess(Ident fieldName, ExprExpression object) {
    this.fieldName = fieldName;
    this.object = object;
  }

  public Ident getFieldName() {
    return fieldName;
  }

  public ExprExpression getObject() {
    return object;
  }

  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

}
