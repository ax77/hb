package njast.ast_parsers;

import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_nodes.clazz.TypeParameters;
import njast.parse.Parse;

public class ParseTypeParameters {
  private final Parse parser;

  public ParseTypeParameters(Parse parser) {
    this.parser = parser;
  }

  public TypeParameters parse() {

    // class Thing<K, V> {
    // ...........^

    TypeParameters tp = new TypeParameters();

    if (parser.is(T.T_LT)) {
      parseTypeParameters(tp);
    }

    return tp;
  }

  private void parseTypeParameters(TypeParameters tp) {
    Token open = parser.checkedMove(T.T_LT);

    tp.put(parser.getIdent());
    while (parser.is(T.T_COMMA)) {
      parser.move();
      tp.put(parser.getIdent());
    }

    Token close = parser.checkedMove(T.T_GT);
  }
}
