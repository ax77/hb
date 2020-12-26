package njast.ast_parsers;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_class.ClassDeclaration;
import njast.ast_class.ConstructorDeclaration;
import njast.ast_class.FieldDeclaration;
import njast.ast_class.MethodDeclaration;
import njast.ast_top.TypeDeclaration;
import njast.parse.Parse;
import njast.symtab.IdentMap;

public class ParseTypeDeclarationsList {
  private final Parse parser;

  public ParseTypeDeclarationsList(Parse parser) {
    this.parser = parser;
  }

  public TypeDeclaration parse() {
    Token tok = parser.moveget();

    if (tok.isIdent(IdentMap.class_ident)) {

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

  private boolean isConstructorDeclaration(ClassDeclaration classBody) {
    final Ident identifier = classBody.getIdentifier();
    return parser.is(T.TOKEN_IDENT) && parser.tok().getIdent().equals(identifier);
  }

  private void putConstructorOrFieldOrMethodIntoClass(ClassDeclaration classBody) {

    // 1) constructor
    // 
    if (isConstructorDeclaration(classBody)) {
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
