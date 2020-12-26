package njast.ast_class;

import java.util.List;

import jscan.symtab.Ident;
import njast.types.Type;

public class MethodDeclaration {

  // header
  private final Ident identifier;
  private List<FormalParameter> formalParameters;

  // private ResultType resultType;
  private Type resultType;
  private boolean isVoid;

  // TODO: body
  // private MethodBody methodBody;

  public MethodDeclaration(Ident identifier) {
    this.identifier = identifier;
    this.isVoid = true;
  }

}
