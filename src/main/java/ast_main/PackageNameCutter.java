package ast_main;

import parse.Parse;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public abstract class PackageNameCutter {

  public static String cutPackageName(Parse parser, Ident id) {

    final Token kw = parser.checkedMove(id); // import/package
    final StringBuilder sb = new StringBuilder();

    while (!parser.isEof()) {

      boolean isOk = parser.is(T.TOKEN_IDENT) || parser.is(T.T_DOT) || parser.is(T.T_SEMI_COLON);
      if (!isOk) {
        parser.perror("expect import path like: [import package.file;]");
      }
      if (parser.is(T.T_SEMI_COLON)) {
        break;
      }
      if (parser.isEof()) {
        parser.perror("unexpected EOF");
      }

      Token importname = parser.moveget();
      if (importname.ofType(T.T_DOT)) {
        sb.append("/");
      } else {
        sb.append(importname.getValue());
      }

    }

    parser.semicolon();
    return sb.toString();
  }

}
