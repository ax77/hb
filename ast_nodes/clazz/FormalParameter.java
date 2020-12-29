package njast.ast_nodes.clazz;

import jscan.symtab.Ident;
import njast.types.Type;

public class FormalParameter {

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
