package njast.ast_class;

import jscan.symtab.Ident;
import njast.ast_flow.BlockStatements;

public class ConstructorDeclaration {

  //  <constructor declaration> ::= <constructor modifiers>? <constructor declarator> <throws>? <constructor body>
  //
  //  <constructor modifiers> ::= <constructor modifier> | <constructor modifiers> <constructor modifier>
  //
  //  <constructor modifier> ::= public | protected | private
  //
  //  <constructor declarator> ::= <simple type name> ( <formal parameter list>? )
  //
  //  <constructor body> ::= { <explicit constructor invocation>? <block statements>? }

  private final Ident identifier;
  private final FormalParameterList formalParameterList;
  private final BlockStatements blockStatements;

  public ConstructorDeclaration(Ident identifier, FormalParameterList formalParameterList,
      BlockStatements blockStatements) {
    this.identifier = identifier;
    this.formalParameterList = formalParameterList;
    this.blockStatements = blockStatements;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public FormalParameterList getFormalParameterList() {
    return formalParameterList;
  }

  public BlockStatements getBlockStatements() {
    return blockStatements;
  }

}
