package njast.ast_parsers;

import java.util.HashSet;
import java.util.Set;

import jscan.symtab.Ident;
import jscan.tokenize.Token;
import njast.modifiers.Modifiers;
import njast.parse.Parse;
import njast.parse.Pcheckers;

public class ParseModifiers {
  private final Parse parser;

  public ParseModifiers(Parse parser) {
    this.parser = parser;
  }

  public Modifiers parse() {
    Modifiers res = new Modifiers();

    while (Pcheckers.is_any_modifier(parser.tok())) {
      Token tok = parser.moveget();
      res.put(tok);
    }

    return res;
  }

}
