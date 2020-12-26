package njast.ast_class.methods;

import jscan.symtab.Ident;

public class MethodDeclarator {
  private Ident identifier;
  private FormalParameterList formalParameterList;

  public MethodDeclarator() {
  }

  public MethodDeclarator(Ident identifier, FormalParameterList formalParameterList) {
    this.identifier = identifier;
    this.formalParameterList = formalParameterList;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public void setIdentifier(Ident identifier) {
    this.identifier = identifier;
  }

  public FormalParameterList getFormalParameterList() {
    return formalParameterList;
  }

  public void setFormalParameterList(FormalParameterList formalParameterList) {
    this.formalParameterList = formalParameterList;
  }

}
