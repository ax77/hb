package njast.ast_nodes.clazz.methods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import njast.ModTypeNameHeader;

public class FormalParameterList implements Serializable {

  private static final long serialVersionUID = 6989912396760951139L;

  private List<ModTypeNameHeader> parameters;

  public FormalParameterList() {
    this.parameters = new ArrayList<ModTypeNameHeader>();
  }

  public void put(ModTypeNameHeader e) {
    this.parameters.add(e);
  }

  public List<ModTypeNameHeader> getParameters() {
    return parameters;
  }

  public boolean isEqualTo(FormalParameterList another) {
    final int bound = parameters.size();

    final List<ModTypeNameHeader> anotherParameters = another.getParameters();
    if (bound != anotherParameters.size()) {
      return false;
    }

    for (int i = 0; i < bound; i++) {
      ModTypeNameHeader fp1 = parameters.get(i);
      ModTypeNameHeader fp2 = anotherParameters.get(i);
      if (!fp1.getType().isEqualTo(fp2.getType())) {
        return false;
      }
    }

    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");

    for (int i = 0; i < parameters.size(); i++) {
      ModTypeNameHeader param = parameters.get(i);

      sb.append(param.getType().toString());
      sb.append(" ");
      sb.append(param.getIdentifier().getName());

      if (i + 1 < parameters.size()) {
        sb.append(", ");
      }
    }

    sb.append(")");
    return sb.toString();
  }

}
