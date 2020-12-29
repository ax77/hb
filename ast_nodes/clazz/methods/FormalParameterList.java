package njast.ast_nodes.clazz.methods;

import java.util.ArrayList;
import java.util.List;

import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class FormalParameterList implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

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

  public boolean isEqualTo(FormalParameterList another) {
    final int bound = parameters.size();

    final List<FormalParameter> anotherParameters = another.getParameters();
    if (bound != anotherParameters.size()) {
      return false;
    }

    for (int i = 0; i < bound; i++) {
      FormalParameter fp1 = parameters.get(i);
      FormalParameter fp2 = anotherParameters.get(i);
      if (!fp1.getType().isEqualTo(fp2.getType())) {
        return false;
      }
    }

    return true;
  }

}
