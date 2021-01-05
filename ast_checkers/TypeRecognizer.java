package njast.ast_checkers;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.parse.Parse;
import njast.types.Type;
import njast.types.TypeBindings;

public class TypeRecognizer {

  private final Parse parser;

  private boolean isPrimitive;
  private boolean isReference;
  private boolean isTypeParameter;

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
    Type referenceType = getReftype();
    if (!referenceType.isClassRef()) {
      parser.perror("expect class-name, but was: " + referenceType.toString());
    }

    return referenceType;
  }

  private Type getReftype() {

    Ident typeName = parser.getIdent();
    final ClassDeclaration classDeclaration = parser.getCurrentClass();

    // 1) Node<String> - reference
    // 2) Node<T> - type-parameter (in template-class declaration)
    Type referenceType = null;
    if (classDeclaration.hasTypeParameter(typeName)) {
      referenceType = classDeclaration.getTypeParameter(typeName);
    } else {
      referenceType = new Type(parser.getClassType(typeName));
    }
    if (referenceType == null) {
      parser.perror("expect type-parameter or class-name for reference type");
    }

    if (parser.is(T.T_LT)) {
      Token begin = parser.checkedMove(T.T_LT);

      //Type rt = getReftype();
      referenceType.putTypeArgument(getTypeArgument());

      while (parser.is(T.T_COMMA)) {
        parser.move();
        //Type rtrest = getReftype();
        referenceType.putTypeArgument(getTypeArgument());
      }

      // TODO: ambiguous between '>' and '>>' in template arguments
      // if (parser.is(T.T_GT) || parser.is(T.T_RSHIFT)) {
      Token end = parser.checkedMove(T.T_GT);
    }

    return referenceType;
  }

  private Type getTypeArgument() {
    TypeRecognizer nested = new TypeRecognizer(parser);
    return nested.getType();
  }

  private Type getTypeParameter() {
    Token typenameT = parser.checkedMove(T.TOKEN_IDENT);
    if (!typenameT.ofType(T.TOKEN_IDENT)) {
      parser.perror("expect identifier");
    }
    final ClassDeclaration clazz = parser.getCurrentClass();
    final Type refTypeParameter = clazz.getTypeParameter(typenameT.getIdent());
    if (refTypeParameter == null) {
      parser.perror("unknown type parameter");
    }
    return refTypeParameter;
  }

  private Type getPrimitive() {
    Token tok = parser.checkedMove(T.TOKEN_IDENT);
    Type tp = TypeBindings.BIND_IDENT_TO_TYPE.get(tok.getIdent());
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
