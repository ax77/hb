package njast.ast_nodes.clazz.vars;

import java.util.ArrayList;
import java.util.List;

import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class VarDeclaratorsList implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

  private List<VarDeclarator> variables;

  public VarDeclaratorsList() {
    this.variables = new ArrayList<VarDeclarator>();
  }

  public void put(VarDeclarator e) {
    this.variables.add(e);
  }

  public List<VarDeclarator> getVariables() {
    return variables;
  }

}
