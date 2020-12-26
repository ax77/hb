package njast.ast_parsers;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_class.ClassBody;
import njast.ast_class.ClassBodyDeclaration;
import njast.ast_class.ClassDeclaration;
import njast.ast_class.ClassMemberDeclaration;
import njast.ast_class.fields.FieldDeclaration;
import njast.ast_class.methods.FormalParameterList;
import njast.ast_class.methods.MethodBody;
import njast.ast_class.methods.MethodDeclaration;
import njast.ast_class.methods.MethodDeclarator;
import njast.ast_class.methods.MethodHeader;
import njast.ast_class.methods.ResultType;
import njast.ast_class.vars.VariableDeclarator;
import njast.ast_class.vars.VariableDeclarators;
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

      Ident ident = parser.getIdent();
      ClassBody classBody = parseClassBody();

      TypeDeclaration typeDeclaration = new TypeDeclaration(new ClassDeclaration(ident, classBody));
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

  private ClassBody parseClassBody() {
    Token lbrace = parser.lbrace();

    ClassBody classBody = new ClassBody();

    // class C { }
    // ..........^
    if (parser.is(T.T_RIGHT_BRACE)) {
      return classBody;
    }

    while (!parser.isEof()) {
      ClassBodyDeclaration classBodyDeclaration = parseClassBodyDeclaration();
      classBody.put(classBodyDeclaration);

      if (parser.is(T.T_RIGHT_BRACE)) {
        break;
      }

    }

    Token rbrace = parser.rbrace();

    return classBody;
  }

  private ClassBodyDeclaration parseClassBodyDeclaration() {

    ClassMemberDeclaration classMemberDeclaration = parseClassMemberDeclaration();
    return new ClassBodyDeclaration(classMemberDeclaration);
  }

  //  <field declaration> ::= <field modifiers>? <type> <variable declarators> ;
  //
  //  <field modifiers> ::= <field modifier> | <field modifiers> <field modifier>
  //
  //  <field modifier> ::= public | protected | private | static | final | transient | volatile
  //
  //  <variable declarators> ::= <variable declarator> | <variable declarators> , <variable declarator>
  //
  //  <variable declarator> ::= <variable declarator id> | <variable declarator id> = <variable initializer>
  //
  //  <variable declarator id> ::= <identifier> | <variable declarator id> [ ]
  //
  //  <variable initializer> ::= <expression> | <array initializer>

  private ClassMemberDeclaration parseClassMemberDeclaration() {

    boolean isFunction = new IsFunc(parser).isFunc();
    if (isFunction) {
      MethodDeclaration methodDeclaration = parseMethodDeclaration();
      return new ClassMemberDeclaration(methodDeclaration);
    }

    FieldDeclaration fieldDeclaration = parseFieldDeclaration();
    return new ClassMemberDeclaration(fieldDeclaration);
  }

  private MethodDeclaration parseMethodDeclaration() {

    //    <method declaration> ::= <method header> <method body>
    //    <method header> ::= <method modifiers>? <result type> <method declarator> <throws>?
    //    <result type> ::= <type> | void
    //    <method modifiers> ::= <method modifier> | <method modifiers> <method modifier>
    //    <method modifier> ::= public | protected | private | static | abstract | final | synchronized | native
    //    <method declarator> ::= <identifier> ( <formal parameter list>? )
    //    <method body> ::= <block> | ;

    MethodHeader methodHeader = parseMethodHeader();
    MethodBody methodBody = parseMethodBody();
    return new MethodDeclaration(methodHeader, methodBody);
  }

  private MethodBody parseMethodBody() {
    // TODO:
    parser.lbrace();
    parser.rbrace();
    return new MethodBody();
  }

  private MethodHeader parseMethodHeader() {
    ResultType resultType = parseResultType();
    MethodDeclarator methodDeclarator = parseMethodDeclarator();
    return new MethodHeader(resultType, methodDeclarator);
  }

  private MethodDeclarator parseMethodDeclarator() {
    // <method declarator> ::= <identifier> ( <formal parameter list>? )
    Ident ident = parser.getIdent();
    parser.lparen();
    parser.rparen();
    return new MethodDeclarator(ident, new FormalParameterList());
  }

  private ResultType parseResultType() {
    // <result type> ::= <type> | void

    parser.checkedMove(IdentMap.void_ident);
    ResultType resultType = new ResultType();

    return resultType;
  }

  private FieldDeclaration parseFieldDeclaration() {
    Type type = parseType();
    VariableDeclarators variableDeclarators = parseVariableDeclarators();

    parser.semicolon();
    return new FieldDeclaration(type, variableDeclarators);
  }

  private VariableDeclarators parseVariableDeclarators() {
    VariableDeclarators variableDeclarators = new VariableDeclarators();

    VariableDeclarator variableDeclarator = parseVariableDeclarator();
    variableDeclarators.put(variableDeclarator);

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
