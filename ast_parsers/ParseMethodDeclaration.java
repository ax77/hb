package njast.ast_parsers;

import jscan.symtab.Ident;
import jscan.tokenize.Token;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.methods.FormalParameterList;
import njast.ast_nodes.stmt.StmtBlock;
import njast.modifiers.Modifiers;
import njast.parse.Parse;
import njast.symtab.IdentMap;
import njast.types.Type;

public class ParseMethodDeclaration {
  private final Parse parser;

  public ParseMethodDeclaration(Parse parser) {
    this.parser = parser;
  }

  public ClassMethodDeclaration parse() {

    //    <method declaration> ::= <method header> <method body>
    //    <method header> ::= <method modifiers>? <result type> <method declarator> <throws>?
    //    <result type> ::= <type> | void
    //    <method modifiers> ::= <method modifier> | <method modifiers> <method modifier>
    //    <method modifier> ::= public | protected | private | static | abstract | final | synchronized | native
    //    <method declarator> ::= <identifier> ( <formal parameter list>? )
    //    <method body> ::= <block> | ;

    Modifiers modifiers = new ParseModifiers(parser).parse();
    Type type = null;

    if (parser.is(IdentMap.void_ident)) {
      Token saved = parser.moveget();
    } else {
      type = new ParseType(parser).parse();
    }

    Ident ident = parser.getIdent();

    FormalParameterList formalParameterList = new ParseFormalParameterList(parser).parse();

    StmtBlock block = new ParseStatement(parser).parseBlock();

    return new ClassMethodDeclaration(null, ident, formalParameterList, block);
  }

}
