package njast.ast_parsers;

import java.util.ArrayList;
import java.util.List;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_class.ClassDeclaration;
import njast.ast_class.ConstructorDeclaration;
import njast.ast_class.FieldDeclaration;
import njast.ast_class.MethodDeclaration;
import njast.ast_class.vars.VariableDeclarator;
import njast.ast_top.TypeDeclaration;
import njast.parse.Parse;
import njast.symtab.IdentMap;
import njast.types.IntegralType;
import njast.types.NumericType;
import njast.types.PrimitiveType;
import njast.types.Type;

public class TypeDeclarationParse {
  private final Parse parser;

  public TypeDeclarationParse(Parse parser) {
    this.parser = parser;
  }

  // <class declaration> ::= <class modifiers>? class <identifier> <super>? <interfaces>? <class body>

  public TypeDeclaration parse() {
    Token tok = parser.moveget();

    if (tok.isIdent(IdentMap.class_ident)) {

      ClassDeclaration classBody = parseClassDeclaration_();

      TypeDeclaration typeDeclaration = new TypeDeclaration(classBody);
      return typeDeclaration;

    }

    parser.perror("unimpl");
    return null;
  }

  //  <class body> ::= { <class body declarations>? }
  //
  //  <class body declarations> ::= <class body declaration> | <class body declarations> <class body declaration>
  //
  //  <class body declaration> ::= <class member declaration> | <static initializer> | <constructor declaration>
  //
  //  <class member declaration> ::= <field declaration> | <method declaration>

  private ClassDeclaration parseClassDeclaration_() {
    Ident ident = parser.getIdent();
    Token lbrace = parser.lbrace();

    ClassDeclaration clazz = new ClassDeclaration(ident);

    // class C { }
    // ..........^
    if (parser.is(T.T_RIGHT_BRACE)) {
      return clazz;
    }

    while (!parser.isEof()) {
      putConstructorOrFieldOrMethodIntoClass(clazz);

      if (parser.is(T.T_RIGHT_BRACE)) {
        break;
      }

    }

    Token rbrace = parser.rbrace();

    return clazz;
  }

  private void putConstructorOrFieldOrMethodIntoClass(ClassDeclaration classBody) {

    // 1) constructor
    // 
    if (parser.is(T.TOKEN_IDENT) && parser.tok().getIdent().equals(classBody.getIdentifier())) {
      Ident identifier = parser.getIdent();
      ConstructorDeclaration constructorDeclaration = new ConstructorDeclaration(identifier);

      // TODO: params
      parser.lparen();
      parser.rparen();

      // TODO: body
      parser.lbrace();
      parser.rbrace();

      classBody.put(constructorDeclaration);
      return;
    }

    // 2) function or field
    //
    boolean isFunction = new IsFunc(parser).isFunc();

    if (isFunction) {
      MethodDeclaration methodDeclaration = parseMethodDeclaration();
      classBody.put(methodDeclaration);
    }

    else {

      FieldDeclaration fieldDeclaration = parseFieldDeclaration();
      classBody.put(fieldDeclaration);
    }

  }

  private MethodDeclaration parseMethodDeclaration() {

    //    <method declaration> ::= <method header> <method body>
    //    <method header> ::= <method modifiers>? <result type> <method declarator> <throws>?
    //    <result type> ::= <type> | void
    //    <method modifiers> ::= <method modifier> | <method modifiers> <method modifier>
    //    <method modifier> ::= public | protected | private | static | abstract | final | synchronized | native
    //    <method declarator> ::= <identifier> ( <formal parameter list>? )
    //    <method body> ::= <block> | ;

    // <result type> ::= <type> | void

    parser.checkedMove(IdentMap.void_ident);

    Ident ident = parser.getIdent();
    MethodDeclaration md = new MethodDeclaration(ident);

    // TODO: params
    parser.lparen();
    parser.rparen();

    // TODO: body
    parser.lbrace();
    parser.rbrace();

    return md;
  }

  private FieldDeclaration parseFieldDeclaration() {
    Type type = parseType();
    List<VariableDeclarator> variableDeclarators = parseVariableDeclarators();

    parser.semicolon();
    return new FieldDeclaration(type, variableDeclarators);
  }

  private List<VariableDeclarator> parseVariableDeclarators() {
    List<VariableDeclarator> variableDeclarators = new ArrayList<VariableDeclarator>();

    VariableDeclarator variableDeclarator = parseVariableDeclarator();
    variableDeclarators.add(variableDeclarator);

    // while(is comma) { rest }

    return variableDeclarators;
  }

  private VariableDeclarator parseVariableDeclarator() {
    Ident id = parser.getIdent();
    return new VariableDeclarator(id);
  }

  private Type parseType() {
    if (!parser.isTypeSpec()) {
      parser.perror("expect int for test");
    }

    Token tok = parser.moveget();
    if (tok.isIdent(IdentMap.int_ident)) {
      return new Type(new PrimitiveType(new NumericType(IntegralType.TP_INT)));
    }

    parser.perror("unimpl");
    return null;
  };

}
