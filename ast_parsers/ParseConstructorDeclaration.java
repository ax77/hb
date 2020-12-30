package njast.ast_parsers;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.ClassConstructorDeclaration;
import njast.ast_nodes.clazz.methods.FormalParameterList;
import njast.ast_nodes.stmt.StmtBlock;
import njast.modifiers.Modifiers;
import njast.parse.Parse;

public class ParseConstructorDeclaration {
  private final Parse parser;

  public ParseConstructorDeclaration(Parse parser) {
    this.parser = parser;
  }

  //  <constructor declaration> ::= <constructor modifiers>? <constructor declarator> <throws>? <constructor body>
  //
  //  <constructor modifiers> ::= <constructor modifier> | <constructor modifiers> <constructor modifier>
  //
  //  <constructor modifier> ::= public | protected | private
  //
  //  <constructor declarator> ::= <simple type name> ( <formal parameter list>? )
  //
  //  <formal parameter list> ::= <formal parameter> | <formal parameter list> , <formal parameter>
  //
  //  <formal parameter> ::= <type> <variable declarator id>
  //
  //  <throws> ::= throws <class type list>
  //
  //  <class type list> ::= <class type> | <class type list> , <class type>
  //
  //  <constructor body> ::= { <explicit constructor invocation>? <block statements>? }

  public ClassConstructorDeclaration parse() {

    Modifiers modifiers = new ParseModifiers(parser).parse();

    Ident identifier = parser.getIdent();

    FormalParameterList formalParameterList = new ParseFormalParameterList(parser).parse();

    StmtBlock block = parseBody();

    return new ClassConstructorDeclaration(identifier, formalParameterList, block);
  }

  private StmtBlock parseBody() {
    StmtBlock blockStatements = new ParseStatement(parser).parseBlock();
    return blockStatements;
  }

}
