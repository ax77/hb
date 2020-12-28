package njast.ast_flow.expr;

import jscan.symtab.Ident;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class GetPointerToClass implements AstTraverser {
  private final Ident classname;

  public GetPointerToClass(Ident classname) {
    this.classname = classname;
  }

  public Ident getClassname() {
    return classname;
  }

  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

}
