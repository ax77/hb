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

    List<Type> tp = new ArrayList<>();

    if (parser.is(T.T_LT)) {
      Token open = parser.checkedMove(T.T_LT);

      tp.add(new Type(parser.getIdent()));
      while (parser.is(T.T_COMMA)) {
        parser.move();
        tp.add(new Type(parser.getIdent()));
      }

      Token close = parser.checkedMove(T.T_GT);
    }

    return tp;
  }
}
