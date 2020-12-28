package njast.ast_parsers;

import jscan.symtab.Ident;
import jscan.tokenize.Token;
import njast.ast_checkers.IsIdent;
import njast.parse.Parse;
import njast.symtab.IdentMap;
import njast.types.PrimitiveType;
import njast.types.ReferenceType;
import njast.types.Type;

public class ParseType {
  private final Parse parser;

  public ParseType(Parse parser) {
    this.parser = parser;
  }

  public Type parse() {
    final boolean isPrimitiveType = IsIdent.isBasicTypeIdent(parser.tok());
    final boolean isReferenceType = parser.isClassName();
    final boolean typeWasFound = isPrimitiveType || isReferenceType;

    if (!typeWasFound) {
      parser.perror("type is not recognized");
    }

    if (isPrimitiveType) {
      Token tok = parser.moveget();
      if (tok.isIdent(IdentMap.int_ident)) {
        return new Type(PrimitiveType.TP_INT);
      }
    }

    Ident typeName = parser.getIdent();
    ReferenceType referenceType = new ReferenceType(typeName);

    return new Type(referenceType);
  }

}
