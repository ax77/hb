package njast.ast_nodes.top;

import java.util.ArrayList;
import java.util.List;

public class TopLevelCompilationUnit {

  private List<TopLevelTypeDeclaration> typeDeclarations;

  public TopLevelCompilationUnit() {
    this.typeDeclarations = new ArrayList<TopLevelTypeDeclaration>();
  }

  public void put(TopLevelTypeDeclaration typeDeclaration) {
    this.typeDeclarations.add(typeDeclaration);
  }

  public List<TopLevelTypeDeclaration> getTypeDeclarations() {
    return typeDeclarations;
  }

}
