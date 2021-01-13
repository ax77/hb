package njast.ast_checkers;

import java.util.ArrayList;
import java.util.List;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.parse.Parse;
import njast.types.Ref;
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

    // I
    if (typeWasFound) {
      this.isPrimitive = true;
    }

    // II
    else {
      typeWasFound = parser.isClassName();
      if (typeWasFound) {
        this.isReference = true;
      }

      // III
      else {
        ClassDeclaration classDeclaration = parser.getCurrentClass(true);
        typeWasFound = classDeclaration.hasTypeParameter(parser.tok().getIdent());
        if (typeWasFound) {
          this.isTypeParameter = true;
        }

        // ???
        else {
        }
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

  private boolean isRefTypenameT(Ident typeName) {
    final ClassDeclaration currentClass = parser.getCurrentClass(true);
    return currentClass.hasTypeParameter(typeName);
  }

  private Type getReftype() {

    // 1) Node<String> - reference
    // 2) Node<T> - type-parameter (in template-class declaration)

    Ident typeName = parser.getIdent();

    if (isRefTypenameT(typeName)) {
      final ClassDeclaration currentClass = parser.getCurrentClass(true);
      return currentClass.getTypeParameter(typeName);
    }

    final List<Type> typeArguments = getTypeArguments();
    final Ref ref = new Ref(parser.getClassType(typeName), typeArguments);
    return new Type(ref);
  }

  public List<Type> getTypeArguments() {
    List<Type> typeArguments = new ArrayList<Type>();

    if (parser.is(T.T_LT)) {
      Token begin = parser.checkedMove(T.T_LT);

      typeArguments.add(getTypeArgument());
      while (parser.is(T.T_COMMA)) {
        parser.move();
        typeArguments.add(getTypeArgument());
      }

      // TODO: ambiguous between '>' and '>>' in template arguments
      // if (parser.is(T.T_GT) || parser.is(T.T_RSHIFT)) {
      Token end = parser.checkedMove(T.T_GT);
    }

    return typeArguments;
  }

  private Type getTypeArgument() {
    TypeRecognizer nested = new TypeRecognizer(parser);
    return nested.getType();
  }

  private Type getTypeParameter() {
    final Token typenameT = parser.checkedMove(T.TOKEN_IDENT);
    final ClassDeclaration clazz = parser.getCurrentClass(true);
    return clazz.getTypeParameter(typenameT.getIdent());
  }

  private Type getPrimitive() {
    Token tok = parser.checkedMove(T.TOKEN_IDENT);
    Type tp = TypeBindings.BIND_IDENT_TO_TYPE.get(tok.getIdent());
    if (tp == null) {
      parser.perror("type is not recognized");
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
