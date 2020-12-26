package njast.ast_class.vars;

import java.util.ArrayList;
import java.util.List;

public class VariableDeclaratorsList {
  private List<VariableDeclarator> variables;

  public VariableDeclaratorsList() {
    this.variables = new ArrayList<VariableDeclarator>();
  }

  public void put(VariableDeclarator e) {
    this.variables.add(e);
  }

  public List<VariableDeclarator> getVariables() {
    return variables;
  }

}
