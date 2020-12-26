package njast.ast_class;

import java.util.ArrayList;
import java.util.List;

import jscan.symtab.Ident;

public class ConstructorDeclaration {

  //  <constructor declaration> ::= <constructor modifiers>? <constructor declarator> <throws>? <constructor body>
  //
  //  <constructor modifiers> ::= <constructor modifier> | <constructor modifiers> <constructor modifier>
  //
  //  <constructor modifier> ::= public | protected | private
  //
  //  <constructor declarator> ::= <simple type name> ( <formal parameter list>? )
  //
  // <constructor body> ::= { <explicit constructor invocation>? <block statements>? }

  private final Ident identifier;
  private List<FormalParameter> formalParameters;

  public ConstructorDeclaration(Ident identifier) {
    this.identifier = identifier;
    this.formalParameters = new ArrayList<FormalParameter>();
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public List<FormalParameter> getFormalParameters() {
    return formalParameters;
  }

}
