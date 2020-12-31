package njast.ast_nodes.clazz.methods;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import njast.ast_nodes.stmt.StmtBlock;
import njast.parse.ILocation;
import njast.parse.NullChecker;
import njast.types.Type;

public class ClassMethodDeclaration implements ILocation {

  private final SourceLocation location;
  private final Type resultType;
  private final Ident identifier;
  private final FormalParameterList formalParameterList;
  private final boolean isVoid;
  private final StmtBlock body;

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

  public boolean isVoid() {
    return isVoid;
  }

  public StmtBlock getBody() {
    return body;
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
    sb.append(formalParameterList.toString());

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
