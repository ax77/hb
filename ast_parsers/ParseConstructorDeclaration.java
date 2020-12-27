package njast.ast_parsers;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_class.ConstructorDeclaration;
import njast.ast_class.FormalParameterList;
import njast.ast_flow.BlockStatements;
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

  public ConstructorDeclaration parse() {

    Modifiers modifiers = new ParseModifiers(parser).parse();

    Ident identifier = parser.getIdent();

    FormalParameterList formalParameterList = new ParseFormalParameterList(parser).parse();

    BlockStatements blockStatements = parseBody();

    return new ConstructorDeclaration(identifier, formalParameterList, blockStatements);
  }

  private BlockStatements parseBody() {
    parser.lbrace();

    if (parser.is(T.T_RIGHT_BRACE)) {
      Token rbrace = parser.moveget();
      return new BlockStatements();
    }

    BlockStatements blockStatements = new ParseStatement(parser).parseBlockStamentList();
    Token rbrace = parser.rbrace();
    return blockStatements;
  }

}
