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

}
