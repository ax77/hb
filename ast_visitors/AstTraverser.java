package njast.ast_visitors;

public interface AstTraverser {
  void accept(AstVisitor visitor);
}
