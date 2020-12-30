package njast.ast_parsers;

import java.util.List;

import jscan.symtab.Ident;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast_checkers.IsConstructor;
import njast.ast_checkers.IsFunc;
import njast.ast_nodes.clazz.ClassConstructorDeclaration;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.ClassFieldDeclaration;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.stmt.StmtBlock;
import njast.ast_nodes.top.TopLevelTypeDeclaration;
import njast.modifiers.Modifiers;
import njast.parse.Parse;
import njast.symtab.IdentMap;

public class ParseTypeDeclarationsList {
  private final Parse parser;

  public ParseTypeDeclarationsList(Parse parser) {
    this.parser = parser;
  }

  public TopLevelTypeDeclaration parse() {

    Modifiers modifiers = new ParseModifiers(parser).parse();

    if (parser.tok().isIdent(IdentMap.class_ident)) {

      Token tok = parser.moveget();

      ClassDeclaration classBody = parseClassDeclaration();

      return new TopLevelTypeDeclaration(classBody);

    }

    parser.perror("unimpl");
    return null;
  }

  private ClassDeclaration parseClassDeclaration() {

    Ident ident = parser.getIdent();
    Token lbrace = parser.lbrace();

    ClassDeclaration clazz = new ClassDeclaration(ident);

    // we'll register this name for type-handling
    // we can't register the whole class, because our compiler is not a one-pass one ;)
    // we'll resolve all methods, fields later, right now we need to know that this simple
    // identifier is a class name, and nothing more...
    // maybe I'm wrong here, and there is more clean and nice way, I'll think about it later.
    //
    parser.defineClassName(clazz);

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

    // 0) static { <block> }
    //
    boolean isStaticInitializer = parser.is(IdentMap.static_ident) && parser.peek().ofType(T.T_LEFT_BRACE);
    if (isStaticInitializer) {
      Token kw = parser.checkedMove(IdentMap.static_ident);

      StmtBlock block = new ParseStatement(parser).parseBlock();
      classBody.put(block);

      return;
    }

    // 1) constructor
    // 
    boolean isConstructorDeclaration = new IsConstructor(parser).isConstructorDeclaration(classBody);
    if (isConstructorDeclaration) {
      ClassConstructorDeclaration constructorDeclaration = new ParseConstructorDeclaration(parser).parse();
      classBody.put(constructorDeclaration);

      return;
    }

    // 2) function or field
    //
    boolean isFunction = new IsFunc(parser).isFunc();

    if (isFunction) {
      ClassMethodDeclaration methodDeclaration = new ParseMethodDeclaration(parser).parse();
      classBody.put(methodDeclaration);
    }

    else {

      List<ClassFieldDeclaration> fieldDeclaration = new ParseFieldDeclaration(parser).parse();
      for (ClassFieldDeclaration field : fieldDeclaration) {
        classBody.put(field);
      }
    }

  }

}
