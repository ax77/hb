package njast.ast_nodes.clazz;

import java.util.ArrayList;
import java.util.List;

public class FormalParameterList {
  private List<FormalParameter> parameters;

  public FormalParameterList() {
    this.parameters = new ArrayList<FormalParameter>();
  }

  public void put(FormalParameter e) {
    this.parameters.add(e);
  }

  public List<FormalParameter> getParameters() {
    return parameters;
  }

}
