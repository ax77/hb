package ast.ast.parsers;

import ast.ast.checkers.IdentRecognizer;
import ast.ast.modifiers.Modifiers;
import ast.parse.Parse;
import jscan.tokenize.Token;

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
