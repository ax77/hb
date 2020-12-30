package njast.ast_nodes.clazz.methods;

import java.util.List;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_visitors.AstTraverser;
import njast.ast_visitors.AstVisitor;
import njast.parse.ILocation;
import njast.parse.NullChecker;
import njast.types.Type;

public class ClassMethodDeclaration implements AstTraverser, ILocation {
  @Override
  public void accept(AstVisitor visitor) {
    visitor.visit(this);
  }

  private final SourceLocation location;

  // header
  private Type resultType;
  private final Ident identifier;
  private final FormalParameterList formalParameterList;
  private final boolean isVoid;

  // body
  private StmtBlock body;

  public ClassMethodDeclaration(Type resultType, Ident identifier, FormalParameterList formalParameterList,
      StmtBlock body, SourceLocation location) {

    NullChecker.check(identifier, formalParameterList, body);

    this.resultType = resultType;
    this.identifier = identifier;
    this.formalParameterList = formalParameterList;
    this.isVoid = (resultType == null);
    this.body = body;
    this.location = location;

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

  @Override
  public SourceLocation getLocation() {
    return location;
  }

  @Override
  public String getLocationToString() {
    return location.toString();
  }

  @Override
  public int getLocationLine() {
    return location.getLine();
  }

  @Override
  public int getLocationColumn() {
    return location.getColumn();
  }

  @Override
  public String getLocationFile() {
    return location.getFilename();
  }

}
