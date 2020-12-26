package njast.ast_class.vars;

import java.util.ArrayList;
import java.util.List;

public class VariableDeclarators {

  private List<VariableDeclarator> variableDeclarators;

  public VariableDeclarators() {
    this.variableDeclarators = new ArrayList<VariableDeclarator>();
  }

  public void put(VariableDeclarator e) {
    this.variableDeclarators.add(e);
  }

  public List<VariableDeclarator> getVariableDeclarators() {
    return variableDeclarators;
  }

}
