package njast.ast_nodes.clazz.vars;

import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;
import njast.types.Type;

public class VarDeclarationLocal implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

  // <local variable declaration> ::= <type> <variable declarators>

  private final Type type;
  private final VarDeclaratorsList vars;

  public VarDeclarationLocal(Type type, VarDeclaratorsList vars) {
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
