package njast.ast_checkers;

import jscan.symtab.Ident;
import jscan.tokenize.Token;
import njast.ast_class.ClassDeclaration;
import njast.ast_parsers.ParseModifiers;
import njast.modifiers.Modifiers;
import njast.parse.Parse;
import njast.parse.ParseState;

public class IsConstructor {
  private final Parse parser;

  public IsConstructor(Parse parser) {
    this.parser = parser;
  }

  public boolean isConstructorDeclaration(ClassDeclaration classBody) {

    ParseState state = new ParseState(parser);

    Modifiers modifiers = new ParseModifiers(parser).parse();

    final Token currentTok = parser.tok();
    final Ident className = classBody.getIdentifier();
    boolean isConstructor = IsIdent.isUserDefinedIdentNoKeyword(currentTok) && currentTok.getIdent().equals(className);

    parser.restoreState(state);
    return isConstructor;
  }

}
