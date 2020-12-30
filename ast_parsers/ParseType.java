package njast.ast_parsers;

import java.util.HashMap;
import java.util.Map;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_checkers.IsIdent;
import njast.parse.Parse;
import njast.symtab.IdentMap;
import njast.types.ReferenceType;
import njast.types.Type;

public class ParseType {
  private final Parse parser;
  private static final Map<Ident, Type> bindings = new HashMap<Ident, Type>();

  static {
    bindings.put(IdentMap.byte_ident, Type.BYTE_TYPE);
    bindings.put(IdentMap.short_ident, Type.SHORT_TYPE);
    bindings.put(IdentMap.char_ident, Type.CHAR_TYPE);
    bindings.put(IdentMap.int_ident, Type.INT_TYPE);
    bindings.put(IdentMap.long_ident, Type.LONG_TYPE);
    bindings.put(IdentMap.float_ident, Type.FLOAT_TYPE);
    bindings.put(IdentMap.double_ident, Type.DOUBLE_TYPE);
    bindings.put(IdentMap.boolean_ident, Type.BOOLEAN_TYPE);
  }

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
      Token tok = parser.checkedMove(T.TOKEN_IDENT);
      Type tp = bindings.get(tok.getIdent());
      if (tp == null) {
        parser.perror("type not recognized");
      }
      return tp;
    }

    Ident typeName = parser.getIdent();
    ReferenceType referenceType = new ReferenceType(parser.getClassType(typeName));

    return new Type(referenceType);
  }

}
