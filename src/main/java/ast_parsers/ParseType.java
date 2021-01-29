package ast_parsers;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_expr.ExpressionBase;
import ast_symtab.Keywords;
import ast_types.ArrayType;
import ast_types.ClassType;
import ast_types.Type;
import ast_types.TypeBindings;
import literals.IntLiteral;
import parse.Parse;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class ParseType {

  private final Parse parser;

  private boolean isPrimitive;
  private boolean isReference;
  private boolean isTypeParameter;
  private boolean isArray;

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

        // IV
        else {
          typeWasFound = parser.is(T.T_LEFT_BRACKET);
          if (typeWasFound) {
            this.isArray = true;
          }

          // V?
          else {

          }
        }
      }
    }

  }

  public Type getType() {

    if (!isType()) {
      parser.perror("type is not recognized");
    }

    // 0)
    if (isArray()) {
      return getArray();
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

  private Type getArray() {
    // 1) fixed   [2:[2:i32]], [2: u64]
    // 2) dynamic [[i32]], [i8]

    parser.checkedMove(T.T_LEFT_BRACKET);

    int count = 0;
    if (parser.is(T.TOKEN_NUMBER)) {
      count = (int) getArrayCount();
      parser.colon();
    }

    Type arrayOf = new ParseType(parser).getType();
    parser.checkedMove(T.T_RIGHT_BRACKET);

    return new Type(new ArrayType(arrayOf, count));
  }

  public long getArrayCount() {
    ExprExpression count = new ParseExpression(parser).e_const_expr();
    if (count.getBase() != ExpressionBase.EPRIMARY_NUMBER) {
      parser.perror("expect array size.");
    }
    //TODO:
    IntLiteral num = count.getNumber();
    // if (!num.isInteger()) {
    //   parser.perror("expect array size integer.");
    // }
    // if (num.getClong() <= 0) {
    //   parser.perror("zero or negative array size are not supported");
    // }
    return num.getInteger();
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
    final ClassType ref = new ClassType(parser.getClassType(typeName), typeArguments);
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
    ParseType nested = new ParseType(parser);
    return nested.getType();
  }

  private Type getTypeParameter() {
    final Token typenameT = parser.checkedMove(T.TOKEN_IDENT);
    final ClassDeclaration clazz = parser.getCurrentClass(true);
    return clazz.getTypeParameter(typenameT.getIdent());
  }

  private Type getPrimitive() {
    Token tok = parser.checkedMove(T.TOKEN_IDENT);
    Type tp = TypeBindings.getTypeFromIdent(tok.getIdent());
    if (tp == null) {
      parser.perror("type is not recognized");
    }
    return tp;
  }

  public boolean isType() {
    return isPrimitive || isReference || isTypeParameter || isArray;
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

  public boolean isArray() {
    return isArray;
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
