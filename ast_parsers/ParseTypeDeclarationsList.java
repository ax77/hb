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
import njast.ast_nodes.clazz.methods.FormalParameterList;
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

  private void putConstructorOrFieldOrMethodIntoClass(ClassDeclaration clazz) {

    // 0) static { <block> }
    //
    boolean isStaticInitializer = parser.is(IdentMap.static_ident) && parser.peek().ofType(T.T_LEFT_BRACE);
    if (isStaticInitializer) {
      Token kw = parser.checkedMove(IdentMap.static_ident);

      StmtBlock block = new ParseStatement(parser).parseBlock();
      clazz.put(block);

      return;
    }

    // 1) constructor
    // 
    boolean isConstructorDeclaration = new IsConstructor(parser).isConstructorDeclaration(clazz);
    if (isConstructorDeclaration) {
      ClassConstructorDeclaration constructorDeclaration = new ParseConstructorDeclaration(parser).parse();
      checkRedefinition(clazz, constructorDeclaration);
      clazz.put(constructorDeclaration);

      return;
    }

    // 2) function or field
    //
    boolean isFunction = new IsFunc(parser).isFunc();

    if (isFunction) {
      ClassMethodDeclaration methodDeclaration = new ParseMethodDeclaration(parser).parse();
      checkRedefinition(clazz, methodDeclaration);
      clazz.put(methodDeclaration);
    }

    else {

      List<ClassFieldDeclaration> fieldDeclaration = new ParseFieldDeclaration(parser).parse(clazz);
      for (ClassFieldDeclaration field : fieldDeclaration) {
        checkRedefinition(clazz, field);
        clazz.put(field);
      }
    }

  }

  private void checkRedefinition(ClassDeclaration clazz, ClassConstructorDeclaration another) {
    for (ClassConstructorDeclaration constructor : clazz.getConstructors()) {
      final FormalParameterList fp1 = constructor.getFormalParameterList();
      final FormalParameterList fp2 = another.getFormalParameterList();
      if (fp1.isEqualTo(fp2)) {
        parser.perror("duplicate constructor with the same formal parameters");
      }
    }
  }

  private void checkRedefinition(ClassDeclaration clazz, ClassMethodDeclaration another) {
    for (ClassMethodDeclaration method : clazz.getMethods()) {
      if (method.getIdentifier().equals(another.getIdentifier())) {
        final FormalParameterList fp1 = method.getFormalParameterList();
        final FormalParameterList fp2 = another.getFormalParameterList();
        if (fp1.isEqualTo(fp2)) {
          parser.perror("duplicate methods with the same formal parameters");
        }
      }
    }
  }

  private void checkRedefinition(ClassDeclaration clazz, ClassFieldDeclaration another) {
    for (ClassFieldDeclaration field : clazz.getFields()) {
      final Ident name1 = field.getField().getIdentifier();
      final Ident name2 = another.getField().getIdentifier();
      if (name1.equals(name2)) {
        parser.perror("duplicate field");
      }
    }
  }

}
