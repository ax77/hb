package njast.ast_class;

import jscan.symtab.Ident;
import njast.types.Type;

public class MethodDeclaration {

  // header
  private final Ident identifier;
  private FormalParameterList formalParameterList;
  private Type resultType;
  private boolean isVoid;

  // TODO: body

  public MethodDeclaration(Ident identifier, Type resultType) {
    this.identifier = identifier;
    this.resultType = resultType;
    this.isVoid = false;
  }

}
