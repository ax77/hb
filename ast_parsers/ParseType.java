package njast.ast_parsers;

import jscan.tokenize.Token;
import njast.ast_checkers.IsIdent;
import njast.parse.Parse;
import njast.symtab.IdentMap;
import njast.types.PrimitiveType;
import njast.types.Type;

public class ParseType {
  private final Parse parser;

  public ParseType(Parse parser) {
    this.parser = parser;
  }

  public Type parse() {
    if (!IsIdent.isBasicTypeIdent(parser.tok())) {
      parser.perror("expect int for test");
    }

    Token tok = parser.moveget();
    if (tok.isIdent(IdentMap.int_ident)) {
      return new Type(PrimitiveType.TP_INT);
    }

    parser.perror("unimpl");
    return null;
  }

}
