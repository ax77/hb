package njast.ast_class;

import jscan.symtab.Ident;
import njast.ast_flow.CStatement;
import njast.types.Type;

public class MethodDeclaration {

  // header
  private final Ident identifier;
  private FormalParameterList formalParameterList;
  private Type resultType;
  private boolean isVoid;

  // body
  private CStatement body;

  public MethodDeclaration(Ident identifier, Type resultType) {
    this.identifier = identifier;
    this.resultType = resultType;
    this.isVoid = false;
  }

  public FormalParameterList getFormalParameterList() {
    return formalParameterList;
  }

  public void setFormalParameterList(FormalParameterList formalParameterList) {
    this.formalParameterList = formalParameterList;
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

  public void setVoid(boolean isVoid) {
    this.isVoid = isVoid;
  }

  public CStatement getBody() {
    return body;
  }

  public void setBody(CStatement body) {
    this.body = body;
  }

  public Ident getIdentifier() {
    return identifier;
  }

}
