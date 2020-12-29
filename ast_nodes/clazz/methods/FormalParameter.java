package njast.ast_nodes.clazz.methods;

import jscan.symtab.Ident;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;
import njast.types.Type;

public class FormalParameter implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

  //  <formal parameter list> ::= <formal parameter> | <formal parameter list> , <formal parameter>
  //
  //  <formal parameter> ::= <type> <variable declarator id>

  private final Type type;
  private final Ident name;

  public FormalParameter(Type type, Ident name) {
    this.type = type;
    this.name = name;
  }

  public Type getType() {
    return type;
  }

  public Ident getName() {
    return name;
  }

}
