package njast.ast_parsers;

import jscan.symtab.Ident;
import njast.ast_class.MethodDeclaration;
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

    Type type = new ParseType(parser).parse();
    Ident ident = parser.getIdent();

    MethodDeclaration md = new MethodDeclaration(ident, type);

    // TODO: params
    parser.lparen();
    parser.rparen();

    // TODO: body
    parser.lbrace();
    parser.rbrace();

    return md;
  }

}
