package njast.ast_nodes.clazz.methods;

import java.util.List;

import jscan.symtab.Ident;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;
import njast.parse.NullChecker;
import njast.types.Type;

public class ClassMethodDeclaration implements AstTraverser {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

  // header
  private Type resultType;
  private final Ident identifier;
  private final FormalParameterList formalParameterList;
  private final boolean isVoid;

  // body
  private StmtBlock body;

  public ClassMethodDeclaration(Type resultType, Ident identifier, FormalParameterList formalParameterList,
      StmtBlock body) {

    NullChecker.check(identifier, formalParameterList, body);

    this.resultType = resultType;
    this.identifier = identifier;
    this.formalParameterList = formalParameterList;
    this.isVoid = (resultType == null);
    this.body = body;

  }

  public FormalParameterList getFormalParameterList() {
    return formalParameterList;
  }

  public Type getResultType() {
    return resultType;
  }

  public void setResultType(Type resultType) {
    this.resultType = resultType;
  }

  public boolean isVoid() {
    return isVoid;
  }

  public StmtBlock getBody() {
    return body;
  }

  public void setBody(StmtBlock body) {
    this.body = body;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (isVoid) {
      sb.append("VOID");
    } else {
      sb.append(resultType.toString());
    }
    sb.append(" ");
    sb.append(identifier.getName());
    sb.append(fp());

    return sb.toString();
  }

  private String fp() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");

    final List<FormalParameter> parameters = formalParameterList.getParameters();
    for (int i = 0; i < parameters.size(); i++) {
      FormalParameter param = parameters.get(i);

      sb.append(param.getName().getName());
      sb.append(": ");
      sb.append(param.getType().toString());

      if (i + 1 < parameters.size()) {
        sb.append(", ");
      }
    }

    sb.append(")");
    return sb.toString();
  }

}
