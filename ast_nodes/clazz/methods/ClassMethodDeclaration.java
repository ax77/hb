package njast.ast_nodes.clazz.methods;

import java.io.Serializable;

import jscan.symtab.Ident;
import njast.IModTypeNameHeader;
import njast.ModTypeNameHeader;
import njast.ast_nodes.stmt.StmtBlock;
import njast.parse.NullChecker;
import njast.types.Type;

public class ClassMethodDeclaration implements Serializable, IModTypeNameHeader {

  private static final long serialVersionUID = 2982374768194205119L;

  private final ModTypeNameHeader header;
  private final FormalParameterList parameters;
  private final StmtBlock body;

  public ClassMethodDeclaration(ModTypeNameHeader header, FormalParameterList parameters, StmtBlock body) {

    NullChecker.check(header, parameters, body);

    this.header = header;
    this.parameters = parameters;
    this.body = body;

  }

  public FormalParameterList getFormalParameterList() {
    return parameters;
  }

  public boolean isVoid() {
    return getType().isVoidStub();
  }

  public StmtBlock getBody() {
    return body;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("func ");
    sb.append(getIdentifier().getName());
    sb.append(parameters.toString());

    if (!isVoid()) {
      sb.append(" -> ");
      sb.append(getType().toString());
    }

    sb.append("\n{\n");
    sb.append(body.toString());
    sb.append("\n}\n");

    return sb.toString();
  }

  @Override
  public Type getType() {
    return header.getType();
  }

  @Override
  public Ident getIdentifier() {
    return header.getIdentifier();
  }

  @Override
  public String getLocationToString() {
    return header.getLocationToString();
  }

  public ModTypeNameHeader getHeader() {
    return header;
  }

}
