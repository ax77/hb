package njast.ast_checkers;

import java.util.HashMap;
import java.util.Map;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.parse.Parse;
import njast.symtab.IdentMap;
import njast.types.ReferenceType;
import njast.types.Type;

public class TypeRecognizer {

  private final Parse parser;

  private boolean isPrimitive;
  private boolean isReference;
  private boolean isTypeParameter;

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

  public TypeRecognizer(Parse parser) {

    this.parser = parser;

    // 1) primitive: int, char, etc
    // 2) reference: class-name, interface-name, etc
    // 3) type-variable, i.e. typename T: class Node<T> { T next; }

    boolean typeWasFound = IdentRecognizer.isBasicTypeIdent(parser.tok());

    if (typeWasFound) {
      this.isPrimitive = true;
    }

    else {
      typeWasFound = parser.isClassName();
      if (typeWasFound) {
        this.isReference = true;
      }

      else {
        ClassDeclaration classDeclaration = parser.getCurrentClass();
        if (classDeclaration == null) {
          parser.unreachable("expect current class");
        }
        this.isTypeParameter = classDeclaration.hasTypeParameter(parser.tok().getIdent());
      }
    }

  }

  public Type getType() {
    if (!isType()) {
      parser.perror("type is not recognized");
    }

    // 1)
    if (isPrimitive()) {
      return getPrimitive();
    }

    //2)
    if (isTypeParameter()) {
      return getTypeParameter();
    }

    // 3) class-name
    ReferenceType referenceType = getReftype();
    return new Type(referenceType);
  }

  private ReferenceType getReftype() {
    
    Ident typeName = parser.getIdent();
    final ClassDeclaration classDeclaration = parser.getCurrentClass();

    // 1) Node<String> - reference
    // 2) Node<T> - type-parameter (in template-class declaration)
    ReferenceType referenceType = null;
    if (classDeclaration.hasTypeParameter(typeName)) {
      referenceType = classDeclaration.getTypeParameter(typeName);
    } else {
      referenceType = new ReferenceType(parser.getClassType(typeName));
    }
    if (referenceType == null) {
      parser.perror("expect type-parameter or class-name for reference type");
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

  private Type getTypeParameter() {
    Token typenameT = parser.checkedMove(T.TOKEN_IDENT);
    if (!typenameT.ofType(T.TOKEN_IDENT)) {
      parser.perror("expect identifier");
    }
    final ClassDeclaration clazz = parser.getCurrentClass();
    final ReferenceType refTypeParameter = clazz.getTypeParameter(typenameT.getIdent());
    if (refTypeParameter == null) {
      parser.perror("unknown type parameter");
    }
    return new Type(refTypeParameter);
  }

  private Type getPrimitive() {
    Token tok = parser.checkedMove(T.TOKEN_IDENT);
    Type tp = bindings.get(tok.getIdent());
    if (tp == null) {
      parser.perror("type not recognized");
    }
    return tp;
  }

  public boolean isType() {
    return isPrimitive || isReference || isTypeParameter;
  }

  public boolean isPrimitive() {
    return isPrimitive;
  }

  public boolean isReference() {
    return isReference;
  }

  public boolean isTypeParameter() {
    return isTypeParameter;
  }

}
