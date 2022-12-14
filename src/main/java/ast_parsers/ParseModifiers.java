package ast_parsers;

import ast_modifiers.Modifiers;
import ast_modifiers.ModifiersChecker;
import parse.Parse;
import tokenize.T;
import tokenize.Token;

public class ParseModifiers {
  private final Parse parser;

  public ParseModifiers(Parse parser) {
    this.parser = parser;
  }

  public Modifiers parse() {
    Modifiers mods = new Modifiers();

    while (ModifiersChecker.isAnyModifier(parser.tok())) {
      final Token tok = parser.checkedMove(T.TOKEN_IDENT);
      if (mods.contains(tok.getIdent())) {
        parser.perror("duplicate modifier: " + tok.getValue());
      }
      mods.put(tok);
    }

    return mods;
  }

}
