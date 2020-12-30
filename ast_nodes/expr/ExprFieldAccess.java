package njast.ast_nodes.expr;

import jscan.symtab.Ident;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;
import njast.ast_visitors.Symbol;

public class ExprFieldAccess implements AstTraverser {
  private final Ident fieldName;
  private final ExprExpression object;

  private Symbol symField; // MARK:HIR
  private Symbol symObject; // MARK:HIR

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

  public Symbol getSymField() {
    return symField;
  }

  public void setSymField(Symbol symField) {
    this.symField = symField;
  }

  public Symbol getSymObject() {
    return symObject;
  }

  public void setSymObject(Symbol symObject) {
    this.symObject = symObject;
  }

  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

}
