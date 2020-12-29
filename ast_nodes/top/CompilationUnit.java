package njast.ast_nodes.top;

import java.util.ArrayList;
import java.util.List;

public class CompilationUnit {
  private List<TypeDeclaration> typeDeclarations;

  public CompilationUnit() {
    this.typeDeclarations = new ArrayList<TypeDeclaration>();
  }

  public void put(TypeDeclaration typeDeclaration) {
    this.typeDeclarations.add(typeDeclaration);
  }

  public List<TypeDeclaration> getTypeDeclarations() {
    return typeDeclarations;
  }

}
