package ast_parsers;

import ast_checkers.IdentRecognizer;
import ast_modifiers.Modifiers;
import parse.Parse;
import tokenize.Token;

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
