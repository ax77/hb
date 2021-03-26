package ast_parsers;

import ast_symtab.Keywords;
import errors.AstParseException;
import parse.Parse;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public abstract class ParsePackageName {

  public static String parse(Parse parser, Ident id) {

    boolean isOkStart = id.equals(Keywords.import_ident);
    if (!isOkStart) {
      throw new AstParseException("unexpected id: " + id.getName());
    }

    @SuppressWarnings("unused")
    final Token beginPos = parser.checkedMove(id);

    final StringBuilder sb = new StringBuilder();

    while (!parser.isEof()) {

      boolean isOk = parser.is(T.TOKEN_IDENT) || parser.is(T.T_DOT) || parser.is(T.T_SEMI_COLON)
          || parser.is(T.T_COLON_COLON);
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
      sb.append(importname.getValue());

    }

    parser.semicolon();
    return sb.toString();
  }

}
