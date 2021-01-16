package njast.ast.parsers;

import jscan.tokenize.Token;
import njast.ast.checkers.IdentRecognizer;
import njast.ast.modifiers.Modifiers;
import njast.parse.Parse;

public class ParseModifiers {
  private final Parse parser;

  public ParseModifiers(Parse parser) {
    this.parser = parser;
  }

  public Modifiers parse() {
    Modifiers res = new Modifiers();

    while (IdentRecognizer.is_any_modifier(parser.tok())) {
      Token tok = parser.moveget();
      res.put(tok);
    }

    return res;
  }

}
