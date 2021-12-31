package ast_parsers;

import java.util.ArrayList;
import java.util.List;

import ast_types.Type;
import parse.Parse;
import tokenize.T;
import tokenize.Token;

public class ParseTypeParameters {
  private final Parse parser;

  public ParseTypeParameters(Parse parser) {
    this.parser = parser;
  }

  public List<Type> parse() {

    // class Thing<K, V> {
    // ...........^

    final List<Type> typenamesT = new ArrayList<>();

    if (parser.is(T.T_LT)) {
      parser.lt();

      typenamesT.add(getOneTypeParameter());
      while (parser.is(T.T_COMMA)) {
        parser.move();
        typenamesT.add(getOneTypeParameter());
      }

      parser.gt();
    }

    return typenamesT;
  }

  private Type getOneTypeParameter() {
    final Token tok = parser.checkedMove(T.TOKEN_IDENT);
    return new Type(tok.getIdent());
  }
}
