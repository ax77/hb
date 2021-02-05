package ast_parsers;

import java.util.ArrayList;
import java.util.List;

import ast_builtins.BuiltinFunctionsCreator;
import ast_builtins.BuiltinNames;
import ast_class.ClassDeclaration;
import ast_symtab.Keywords;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeBindings;
import ast_types.TypeUnresolvedId;
import hashed.Hash_ident;
import parse.Parse;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class ParseType {

  private final Parse parser;

  private boolean isPrimitive;
  private boolean isReference;
  private boolean isTypeParameter;
  private boolean isUnresolvedId;

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
      typeWasFound = parser.isClassName();
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

    if (!typeWasFound) {
      typeWasFound = parser.is(T.T_LEFT_BRACKET);
      if (typeWasFound) {
        parser.errorArray();
      }
    }

    if (!typeWasFound) {
      typeWasFound = parser.isUserDefinedIdentNoKeyword(parser.tok());
      if (typeWasFound) {
        this.isUnresolvedId = true;
      }
    }

  }

  public Type getType() {

    if (!isType()) {
      parser.perror("type is not recognized");
    }

    // 0
    if (isUnresolvedId()) {
      if (parser.is(BuiltinNames.builtin_ident)) {
        return builtinArray();
      }
      return getUnresolvedIdentifier();
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
    if (!referenceType.is_class()) {
      parser.perror("expect class-name, but was: " + referenceType.toString());
    }

    return referenceType;
  }

  private Type builtinArray() {

    // array<T> class
    final ClassDeclaration currentc = parser.getCurrentClass(true);
    if (!currentc.getIdentifier().equals(BuiltinNames.array_ident)) {
      parser.perror("you cannot use builtin.array, this type is predefined only for array<T> class");
    }

    final Token beginPos = parser.checkedMove(BuiltinNames.builtin_ident);

    parser.checkedMove(T.T_DOT);
    parser.checkedMove(BuiltinNames.array_declare_ident);

    final List<Type> arguments = getTypeArguments();
    if (arguments.size() != 1) {
      parser.perror("expect type argument for array.");
    }

    // TODO: how about type-setters?
    return new Type(arguments.get(0), beginPos);
  }

  private Type getUnresolvedIdentifier() {
    final Token beginPos = parser.checkedMove(T.TOKEN_IDENT);
    final Ident typeName = beginPos.getIdent();
    final List<Type> typeArguments = getTypeArguments();
    final TypeUnresolvedId unresolvedId = new TypeUnresolvedId(typeName, typeArguments, beginPos);
    return new Type(unresolvedId, beginPos);
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
    final ClassTypeRef ref = new ClassTypeRef(parser.getClassType(typeName), typeArguments);
    return new Type(ref, tok);
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
    final Type tp = TypeBindings.getTypeFromIdent(tok);
    if (tp == null) {
      parser.perror("type is not recognized");
    }
    return tp;
  }

  public boolean isType() {
    return isPrimitive || isReference || isTypeParameter || isUnresolvedId;
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

  public boolean isUnresolvedId() {
    return isUnresolvedId;
  }

  //@formatter:off
  private boolean isBasicTypeIdent(Token what) {
    return what.isIdent(Keywords.i8_ident )
        || what.isIdent(Keywords.u8_ident )
        || what.isIdent(Keywords.i16_ident)
        || what.isIdent(Keywords.u16_ident)
        || what.isIdent(Keywords.i32_ident)
        || what.isIdent(Keywords.u32_ident)
        || what.isIdent(Keywords.i64_ident)
        || what.isIdent(Keywords.u64_ident)
        || what.isIdent(Keywords.f32_ident)
        || what.isIdent(Keywords.f64_ident)
        || what.isIdent(Keywords.boolean_ident);
  }
  //@formatter:on

}
