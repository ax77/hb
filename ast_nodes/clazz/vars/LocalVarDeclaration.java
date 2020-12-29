package njast.ast_nodes.clazz.vars;

import njast.types.Type;

public class LocalVarDeclaration {

  // <local variable declaration> ::= <type> <variable declarators>

  private final Type type;
  private final VarDeclaratorsList vars;

  public LocalVarDeclaration(Type type, VarDeclaratorsList vars) {
    this.type = type;
    this.vars = vars;
  }

  public Type getType() {
    return type;
  }

  public VarDeclaratorsList getVars() {
    return vars;
  }

}
