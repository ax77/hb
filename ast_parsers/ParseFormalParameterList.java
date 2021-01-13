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

  // func name(param: int) -> int {  }

  public FormalParameterList parse() {
    FormalParameterList parameters = new FormalParameterList();
    Token lparen = parser.lparen();

    if (parser.is(T.T_RIGHT_PAREN)) {
      Token rparen = parser.rparen();
      return parameters;
    }

    parameters.put(parseOneParam());
    while (parser.is(T.T_COMMA)) {
      Token comma = parser.moveget();
      parameters.put(parseOneParam());
    }

    Token rparen = parser.rparen();
    return parameters;
  }

  private ModTypeNameHeader parseOneParam() {
    final Token tok = parser.checkedMove(T.TOKEN_IDENT);
    final Ident id = tok.getIdent();
    final Token colon = parser.colon();
    final Type type = new TypeRecognizer(parser).getType();
    return new ModTypeNameHeader(new Modifiers(), type, id, new SourceLocation(tok));
  }

}
