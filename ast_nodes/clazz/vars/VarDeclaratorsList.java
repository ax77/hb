package njast.ast_nodes.clazz.vars;

import java.util.ArrayList;
import java.util.List;

public class VarDeclaratorsList {
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
