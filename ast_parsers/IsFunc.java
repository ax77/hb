package njast.ast_parsers;

import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.modifiers.Modifiers;
import njast.parse.Parse;
import njast.parse.ParseState;

public class IsFunc {

  private final Parse parser;

  public IsFunc(Parse parser) {
    this.parser = parser;
  }

  public boolean isFunc() {

    ParseState state = new ParseState(parser);
    boolean result = isFuncInternal();

    parser.restoreState(state);
    return result;
  }

  private boolean isFuncInternal() {

    //
    //  1) declaration's
    //
    //  int a                  ;
    //  int a, b, c            ;
    //  int a = 1              ;
    //  int[] a1 = { 10 }      ;
    //  int a2[]               ;
    //  int[] a3 = new int[20] ;
    //
    //  2) function's
    //
    //  int f(int a) {  }
    //  int f()      {  }
    //

    // try to do it lexically, instead of mess guess.

    while (parser.tp() == T.T_SEMI_COLON) {
      parser.move();
    }

    Modifiers modifiers = new ParseModifiers(parser).parse();

    boolean hasOpen = false;
    boolean hasClose = false;

    while (!parser.isEof()) {

      Token t = parser.moveget();

      if (!hasOpen) {
        hasOpen = t.ofType(T.T_LEFT_PAREN);
      }
      if (!hasClose) {
        hasClose = t.ofType(T.T_RIGHT_PAREN);
      }

      if (t.ofType(T.T_ASSIGN)) {
        return false;
      } else if (t.ofType(T.T_SEMI_COLON)) {
        return false;
      } else if (t.ofType(T.T_LEFT_BRACE)) {
        return hasOpen && hasClose;
      }
    }

    parser.unreachable("don't know: var/func");
    return false;
  }

}
