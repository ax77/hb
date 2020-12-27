package njast.ast_class;

import jscan.symtab.Ident;
import njast.ast_flow.Block;
import njast.types.Type;

public class MethodDeclaration {

  // header
  private Type resultType;
  private final Ident identifier;
  private FormalParameterList formalParameterList;
  private boolean isVoid;

  // body
  private Block body;

  public MethodDeclaration(Type resultType, Ident identifier, FormalParameterList formalParameterList, Block body) {

    this.resultType = resultType;
    this.identifier = identifier;
    this.formalParameterList = formalParameterList;
    this.isVoid = false;
    this.body = body;

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

  public Block getBody() {
    return body;
  }

  public void setBody(Block body) {
    this.body = body;
  }

  public Ident getIdentifier() {
    return identifier;
  }

}
