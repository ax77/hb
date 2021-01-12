package njast.ast_parsers;

import jscan.sourceloc.SourceLocation;
import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ModTypeNameHeader;
import njast.ast_checkers.TypeRecognizer;
import njast.ast_nodes.clazz.methods.FormalParameterList;
import njast.modifiers.Modifiers;
import njast.parse.Parse;
import njast.types.Type;

public class ParseFormalParameterList {
  private final Parse parser;

  public ParseFormalParameterList(Parse parser) {
    this.parser = parser;
  }

  //  <formal parameter list> ::= <formal parameter> | <formal parameter list> , <formal parameter>
  //
  //  <formal parameter> ::= <type> <variable declarator id>

  public FormalParameterList parse() {
    FormalParameterList parameters = new FormalParameterList();

    Token lparen = parser.lparen();

    if (parser.is(T.T_RIGHT_PAREN)) {
      Token rparen = parser.rparen();
      return parameters;
    }

    ModTypeNameHeader oneparam = parseOneParam();
    parameters.put(oneparam);

    while (parser.is(T.T_COMMA)) {
      Token comma = parser.moveget();

      ModTypeNameHeader oneparamRest = parseOneParam();
      parameters.put(oneparamRest);
    }

    Token rparen = parser.rparen();

    return parameters;
  }

  private ModTypeNameHeader parseOneParam() {
    final Modifiers modifiers = new ParseModifiers(parser).parse();
    final Type type = new TypeRecognizer(parser, false).getType();
    final Token tok = parser.checkedMove(T.TOKEN_IDENT);
    final Ident name = tok.getIdent();
    return new njast.ModTypeNameHeader(modifiers, type, name, new SourceLocation(tok));
  }

}
