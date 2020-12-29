package njast.ast_nodes.top;

import java.util.ArrayList;
import java.util.List;

import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;

public class TopLevelCompilationUnit implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

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
