package njast.ast_parsers;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_checkers.IsConstructor;
import njast.ast_checkers.IsFunc;
import njast.ast_class.ClassDeclaration;
import njast.ast_class.ConstructorDeclaration;
import njast.ast_class.FieldDeclaration;
import njast.ast_class.MethodDeclaration;
import njast.ast_top.TypeDeclaration;
import njast.modifiers.Modifiers;
import njast.parse.Parse;
import njast.symtab.IdentMap;

public class ParseTypeDeclarationsList {
  private final Parse parser;

  public ParseTypeDeclarationsList(Parse parser) {
    this.parser = parser;
  }

  public TypeDeclaration parse() {

    Modifiers modifiers = new ParseModifiers(parser).parse();

    if (parser.tok().isIdent(IdentMap.class_ident)) {

      Token tok = parser.moveget();

      ClassDeclaration classBody = parseClassDeclaration();

      return new TypeDeclaration(classBody);

    }

    parser.perror("unimpl");
    return null;
  }

  private ClassDeclaration parseClassDeclaration() {

    Ident ident = parser.getIdent();
    Token lbrace = parser.lbrace();

    ClassDeclaration clazz = new ClassDeclaration(ident);

    // class C { }
    // ..........^
    if (parser.is(T.T_RIGHT_BRACE)) {
      Token rbrace = parser.rbrace();
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
    boolean isConstructorDeclaration = new IsConstructor(parser).isConstructorDeclaration(classBody);
    if (isConstructorDeclaration) {
      ConstructorDeclaration constructorDeclaration = new ParseConstructorDeclaration(parser).parse();
      classBody.put(constructorDeclaration);
      return;
    }

    // 2) function or field
    //
    boolean isFunction = new IsFunc(parser).isFunc();

    if (isFunction) {
      MethodDeclaration methodDeclaration = new ParseMethodDeclaration(parser).parse();
      classBody.put(methodDeclaration);
    }

    else {

      FieldDeclaration fieldDeclaration = new ParseFieldDeclaration(parser).parse();
      classBody.put(fieldDeclaration);
    }

  }

}
