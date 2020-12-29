package njast.ast_checkers;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_nodes.clazz.ClassDeclaration;
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
    boolean result = isConstructorInternal(classBody);

    parser.restoreState(state);
    return result;
  }

  private boolean isConstructorInternal(ClassDeclaration classBody) {

    // public class ClassName {
    //     public ClassName() {}
    //     .......^........^
    // }

    Modifiers modifiers = new ParseModifiers(parser).parse();

    final Token currentTok = parser.tok();
    final Ident className = classBody.getIdentifier();
    final Token nextTok = parser.peek();

    return IsIdent.isUserDefinedIdentNoKeyword(currentTok) && currentTok.getIdent().equals(className)
        && nextTok.ofType(T.T_LEFT_PAREN);
  }

}
