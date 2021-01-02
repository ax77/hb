package njast.ast_parsers;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import jscan.tokenize.T;
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

    // public static <K, V> boolean compare(Pair<K, V> p1, Pair<K, V> p2) {

    Token location = parser.tok();

    Modifiers modifiers = new ParseModifiers(parser).parse();

    if (parser.is(T.T_LT)) {
      parser.unimplemented("generic methods");
    }

    Type type = null;
    if (parser.is(IdentMap.void_ident)) {
      Token saved = parser.moveget();
    } else {
      type = new ParseType(parser).parse();
    }

    Ident ident = parser.getIdent();

    FormalParameterList formalParameterList = new ParseFormalParameterList(parser).parse();

    StmtBlock block = new ParseStatement(parser).parseBlock();

    return new ClassMethodDeclaration(type, ident, formalParameterList, block, new SourceLocation(location));
  }

}
