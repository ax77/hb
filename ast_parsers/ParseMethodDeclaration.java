package njast.ast_parsers;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.FormalParameterList;
import njast.ast_nodes.clazz.MethodDeclaration;
import njast.ast_nodes.stmt.Block;
import njast.modifiers.Modifiers;
import njast.parse.Parse;
import njast.types.Type;

public class ParseMethodDeclaration {
  private final Parse parser;

  public ParseMethodDeclaration(Parse parser) {
    this.parser = parser;
  }

  public MethodDeclaration parse() {

    //    <method declaration> ::= <method header> <method body>
    //    <method header> ::= <method modifiers>? <result type> <method declarator> <throws>?
    //    <result type> ::= <type> | void
    //    <method modifiers> ::= <method modifier> | <method modifiers> <method modifier>
    //    <method modifier> ::= public | protected | private | static | abstract | final | synchronized | native
    //    <method declarator> ::= <identifier> ( <formal parameter list>? )
    //    <method body> ::= <block> | ;

    Modifiers modifiers = new ParseModifiers(parser).parse();

    Type type = new ParseType(parser).parse();

    Ident ident = parser.getIdent();

    FormalParameterList formalParameterList = new ParseFormalParameterList(parser).parse();

    Block block = new ParseStatement(parser).parseBlock();

    return new MethodDeclaration(type, ident, formalParameterList, block);
  }

}
