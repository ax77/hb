package njast.ast_parsers;

import java.util.ArrayList;
import java.util.List;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_checkers.TypeRecognizer;
import njast.ast_nodes.ModTypeNameHeader;
import njast.modifiers.Modifiers;
import njast.parse.Parse;
import njast.types.Type;

public class ParseFormalParameterList {
  private final Parse parser;

  public ParseFormalParameterList(Parse parser) {
    this.parser = parser;
  }

  // func name(param: int) -> int {  }

  public List<ModTypeNameHeader> parse() {
    List<ModTypeNameHeader> parameters = new ArrayList<>();
    Token lparen = parser.lparen();

    if (parser.is(T.T_RIGHT_PAREN)) {
      Token rparen = parser.rparen();
      return parameters;
    }

    parameters.add(parseOneParam());
    while (parser.is(T.T_COMMA)) {
      Token comma = parser.moveget();
      parameters.add(parseOneParam());
    }

    Token rparen = parser.rparen();
    return parameters;
  }

  private ModTypeNameHeader parseOneParam() {
    final Token tok = parser.checkedMove(T.TOKEN_IDENT);
    final Ident id = tok.getIdent();
    final Token colon = parser.colon();
    final Type type = new TypeRecognizer(parser).getType();
    return new ModTypeNameHeader(new Modifiers(), type, id);
  }

}
