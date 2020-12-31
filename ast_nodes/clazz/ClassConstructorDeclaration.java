package njast.ast_nodes.clazz;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.methods.FormalParameterList;
import njast.ast_nodes.stmt.StmtBlock;

public class ClassConstructorDeclaration {

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
  private final StmtBlock block;

  public ClassConstructorDeclaration(Ident identifier, FormalParameterList formalParameterList, StmtBlock block) {
    this.identifier = identifier;
    this.formalParameterList = formalParameterList;
    this.block = block;
  }

  public Ident getIdentifier() {
    return identifier;
  }

  public FormalParameterList getFormalParameterList() {
    return formalParameterList;
  }

  public StmtBlock getBlock() {
    return block;
  }

  public boolean isCompatibleRedefinition(ClassConstructorDeclaration another) {
    if (formalParameterList.isEqualTo(another.getFormalParameterList())) {
      return false;
    }
    return true;
  }

}
