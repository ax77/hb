package njast.ast_nodes.clazz.methods;

import java.io.Serializable;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import njast.ast_nodes.stmt.StmtBlock;
import njast.parse.ILocation;
import njast.parse.NullChecker;
import njast.templates.TypeSetter;
import njast.types.Type;

public class ClassMethodDeclaration implements ILocation, Serializable, TypeSetter {

  private static final long serialVersionUID = 2982374768194205119L;

  private final SourceLocation location;
  private Type type;
  private final Ident identifier;
  private final FormalParameterList formalParameterList;
  private final boolean isVoid;
  private final StmtBlock body;

  public ClassMethodDeclaration(Type resultType, Ident identifier, FormalParameterList formalParameterList,
      StmtBlock body, SourceLocation location) {

    NullChecker.check(identifier, formalParameterList, body);

    this.type = resultType;
    this.identifier = identifier;
    this.formalParameterList = formalParameterList;
    this.isVoid = (resultType == null);
    this.body = body;
    this.location = location;

  }

  @Override
  public void setType(Type typeToSet) {
    this.type = typeToSet;
  }

  @Override
  public Type getType() {
    return type;
  }

  public FormalParameterList getFormalParameterList() {
    return formalParameterList;
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
      sb.append("void");
    } else {
      sb.append(type.toString());
    }
    sb.append(" ");
    sb.append(identifier.getName());
    sb.append(formalParameterList.toString());
    sb.append("\n{\n");
    sb.append(body.toString());
    sb.append("\n}\n");

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
