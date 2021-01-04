package njast.ast_parsers;

import java.util.HashMap;
import java.util.Map;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_checkers.IsIdent;
import njast.ast_nodes.clazz.ClassDeclaration;
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
    boolean typeWasFound = isPrimitiveType || isReferenceType;

    // class-type-parameters: class C<T> { T x; }
    boolean isFromTypeParameters = false;
    if (!typeWasFound) {
      ClassDeclaration classDeclaration = parser.getCurrentClass();
      if (classDeclaration == null) {
        parser.unreachable("expect current class");
      }
      typeWasFound = classDeclaration.hasTypeParameter(parser.tok().getIdent());
      isFromTypeParameters = true;
    }

    if (!typeWasFound) {
      parser.perror("type is not recognized");
    }

    // 1) generic-stub
    if (isFromTypeParameters) {
      Token tok = parser.checkedMove(T.TOKEN_IDENT);
      if (!tok.ofType(T.TOKEN_IDENT)) {
        parser.perror("expect identifier");
      }
      final ClassDeclaration classDeclaration = parser.getCurrentClass();
      final ReferenceType refTypeParameter = classDeclaration.getTypeParameter(parser.tok().getIdent());
      return new Type(refTypeParameter);
    }

    // 2) int/char/etc
    if (isPrimitiveType) {
      Token tok = parser.checkedMove(T.TOKEN_IDENT);
      Type tp = bindings.get(tok.getIdent());
      if (tp == null) {
        parser.perror("type not recognized");
      }
      return tp;
    }

    //  ReferenceType:
    //    Identifier [TypeArguments] { . Identifier [TypeArguments] }
    //  TypeArguments:
    //    < TypeArgument { , TypeArgument } >
    //  TypeArgument:
    //    ReferenceType

    // 3) class-name
    ReferenceType referenceType = getReftype();
    return new Type(referenceType);
  }

  private ReferenceType getReftype() {
    Ident typeName = parser.getIdent();
    final ClassDeclaration classDeclaration = parser.getCurrentClass();

    ReferenceType referenceType = null;
    if (classDeclaration.hasTypeParameter(typeName)) {
      referenceType = classDeclaration.getTypeParameter(typeName);
    } else {
      referenceType = new ReferenceType(parser.getClassType(typeName));
    }

    if (parser.is(T.T_LT)) {
      Token begin = parser.checkedMove(T.T_LT);

      ReferenceType rt = getReftype();
      referenceType.putTypeArgument(rt);

      while (parser.is(T.T_COMMA)) {
        parser.move();
        ReferenceType rtrest = getReftype();
        referenceType.putTypeArgument(rtrest);
      }

      // TODO: ambiguous between '>' and '>>' in template arguments
      // if (parser.is(T.T_GT) || parser.is(T.T_RSHIFT)) {
      Token end = parser.checkedMove(T.T_GT);
    }

    return referenceType;
  }

}
