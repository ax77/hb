package ast_parsers;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_main.imports.GlobalSymtab;
import ast_symtab.Keywords;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeBindings;
import parse.Parse;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class ParseType {

  private final Parse parser;

  private boolean isPrimitive;
  private boolean isReference;
  private boolean isTypeParameter;

  public ParseType(Parse parser) {

    this.parser = parser;

    // 1) primitive: int, char, etc
    // 2) reference: class-name, interface-name, etc
    // 3) type-variable, i.e. typename T: class Node<T> { T next; }

    boolean typeWasFound = isBasicTypeIdent(parser.tok());

    // I
    if (typeWasFound) {
      this.isPrimitive = true;
    }

    if (!typeWasFound) {
      typeWasFound = GlobalSymtab.isClassName(parser.tok());
      if (typeWasFound) {
        this.isReference = true;
      }
    }

    if (!typeWasFound) {
      ClassDeclaration classDeclaration = parser.getCurrentClass(true);
      typeWasFound = classDeclaration.hasTypeParameter(parser.tok().getIdent());
      if (typeWasFound) {
        this.isTypeParameter = true;
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
    if (!referenceType.isClass()) {
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

    Token tok = parser.checkedMove(T.TOKEN_IDENT);
    Ident typeName = tok.getIdent();

    if (isRefTypenameT(typeName)) {
      final ClassDeclaration currentClass = parser.getCurrentClass(true);
      return currentClass.getTypeParameter(typeName);
    }

    final List<Type> typeArguments = getTypeArguments();
    final ClassTypeRef ref = new ClassTypeRef(GlobalSymtab.getClassType(parser, typeName), typeArguments);
    return new Type(ref);
  }

  public List<Type> getTypeArguments() {
    List<Type> typeArguments = new ArrayList<Type>();

    if (parser.is(T.T_LT)) {
      parser.lt();

      typeArguments.add(getTypeArgument());
      while (parser.is(T.T_COMMA)) {
        parser.move();
        typeArguments.add(getTypeArgument());
      }

      parser.gt();
    }

    return typeArguments;
  }

  private Type getTypeArgument() {
    ParseType nested = new ParseType(parser);
    return nested.getType();
  }

  private Type getTypeParameter() {
    final Token typenameT = parser.checkedMove(T.TOKEN_IDENT);
    final ClassDeclaration clazz = parser.getCurrentClass(true);
    return clazz.getTypeParameter(typenameT.getIdent());
  }

  private Type getPrimitive() {
    final Token tok = parser.checkedMove(T.TOKEN_IDENT);
    final Type tp = TypeBindings.getTypeFromIdent(tok.getIdent());
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

  //@formatter:off
  private boolean isBasicTypeIdent(Token what) {
    return what.isIdent(Keywords.char_ident )
        || what.isIdent(Keywords.short_ident)
        || what.isIdent(Keywords.int_ident)
        || what.isIdent(Keywords.long_ident)
        || what.isIdent(Keywords.float_ident)
        || what.isIdent(Keywords.double_ident)
        || what.isIdent(Keywords.boolean_ident);
  }
  //@formatter:on

}
