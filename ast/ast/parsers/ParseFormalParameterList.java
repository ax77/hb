package ast.ast.parsers;

import java.util.ArrayList;
import java.util.List;

import ast.ast.checkers.TypeRecognizer;
import ast.ast.nodes.method.MethodParameter;
import ast.parse.Parse;
import ast.types.Type;
import jscan.tokenize.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class ParseFormalParameterList {
  private final Parse parser;

  public ParseFormalParameterList(Parse parser) {
    this.parser = parser;
  }

  // func name(param: int) -> int {  }

  public List<MethodParameter> parse() {
    List<MethodParameter> parameters = new ArrayList<>();
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

  private MethodParameter parseOneParam() {
    final Token tok = parser.checkedMove(T.TOKEN_IDENT);
    final Ident id = tok.getIdent();
    final Token colon = parser.colon();
    final Type type = new TypeRecognizer(parser).getType();
    return new MethodParameter(id, type);
  }

}
