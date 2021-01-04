package njast.ast_parsers;

import java.util.ArrayList;
import java.util.List;

import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.parse.Parse;
import njast.types.ReferenceType;

public class ParseTypeParameters {
  private final Parse parser;

  public ParseTypeParameters(Parse parser) {
    this.parser = parser;
  }

  public List<ReferenceType> parse() {

    // class Thing<K, V> {
    // ...........^

    List<ReferenceType> tp = new ArrayList<ReferenceType>();

    if (parser.is(T.T_LT)) {
      parseTypeParameters(tp);
    }

    return tp;
  }

  private void parseTypeParameters(List<ReferenceType> tp) {
    Token open = parser.checkedMove(T.T_LT);

    tp.add(new ReferenceType(parser.getIdent()));
    while (parser.is(T.T_COMMA)) {
      parser.move();
      tp.add(new ReferenceType(parser.getIdent()));
    }

    Token close = parser.checkedMove(T.T_GT);
  }
}
