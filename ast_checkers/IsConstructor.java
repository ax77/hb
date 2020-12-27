package njast.ast_checkers;

import jscan.symtab.Ident;
import jscan.tokenize.T;
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

    Ident className = classBody.getIdentifier();

    final Token currentTok = parser.tok();

    boolean isConstructor = currentTok.ofType(T.TOKEN_IDENT) && !currentTok.isBuiltinIdent()
        && currentTok.getIdent().equals(className);

    parser.restoreState(state);
    return isConstructor;
  }

}
