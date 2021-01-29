package ast_parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ast_checkers.IdentRecognizer;
import ast_class.ClassDeclaration;
import ast_main.PackageNameCutter;
import ast_method.ClassMethodBase;
import ast_method.ClassMethodDeclaration;
import ast_method.MethodParameter;
import ast_modifiers.Modifiers;
import ast_stmt.StmtBlock;
import ast_symtab.Keywords;
import ast_types.Type;
import ast_unit.CompilationUnit;
import ast_unit.TypeDeclaration;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import parse.Parse;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class ParseTypeDeclarationsList {
  private final Parse parser;

  public ParseTypeDeclarationsList(Parse parser) {
    this.parser = parser;
  }

  public void parse(CompilationUnit unit) throws IOException {

    // opt
    final Modifiers modifiers = new ParseModifiers(parser).parse();

    if (parser.is(Keywords.class_ident)) {
      unit.put(new TypeDeclaration(parseClassDeclaration()));
    } else if (parser.is(Keywords.import_ident)) {
      PackageNameCutter.cutPackageName(parser, Keywords.import_ident);
    } else {
      parser.perror("unimpl");
    }
  }

  private ClassDeclaration parseClassDeclaration() {

    final Token classKw = parser.checkedMove(Keywords.class_ident);
    final Ident ident = parser.getIdent();

    // class Thing<T> {
    // ......^....^
    final List<Type> tp = parseTypeParametersT(); // maybe empty

    final Token lbrace = parser.lbrace();

    // we'll register this name for type-handling
    // we can't register the whole class, because our compiler is not a one-pass one ;)
    // we'll resolve all methods, fields later, right now we need to know that this simple
    // identifier is a class name, and nothing more...
    // maybe I'm wrong here, and there is more clean and nice way, I'll think about it later.
    //

    final ClassDeclaration clazz = parser.getClassType(ident);
    clazz.setTypeParametersT(tp);
    parser.setCurrentClass(clazz);

    // class C { }
    // ..........^
    if (parser.is(T.T_RIGHT_BRACE)) {
      Token rbrace = parser.rbrace();
      parser.setCurrentClass(null);
      return clazz;
    }

    while (!parser.isEof()) {
      putConstructorOrFieldOrMethodIntoClass(clazz);

      if (parser.is(T.T_RIGHT_BRACE)) {
        break;
      }

    }

    Token rbrace = parser.rbrace();

    parser.setCurrentClass(null);
    return clazz;
  }

  private void putConstructorOrFieldOrMethodIntoClass(ClassDeclaration clazz) {

    if (parser.is(Keywords.init_ident)) {
      putConstructor(clazz);
    } else if (parser.is(Keywords.deinit_ident)) {
      putDestructor(clazz);
    } else if (parser.is(Keywords.func_ident)) {
      putMethod(clazz);
    } else {
      putField(clazz);
    }
  }

  private void putField(ClassDeclaration clazz) {

    // var
    // let
    // weak var
    // private var
    // ...

    checkShouldBeTheField();

    final VarDeclarator field = new ParseVarDeclarator(parser).parse(VarBase.CLASS_FIELD);
    field.setClazz(clazz);

    checkFieldRedefinition(clazz, field);
    clazz.addField(field);

  }

  private void checkShouldBeTheField() {
    final boolean isOk = IdentRecognizer.is_any_modifier(parser.tok());
    if (!isOk) {
      parser.perror("expect class-field, but was: " + parser.tok().getValue());
    }
  }

  private void putMethod(ClassDeclaration clazz) {
    final Token beginPos = parser.checkedMove(Keywords.func_ident);
    final Ident ident = parser.getIdent();
    final List<MethodParameter> parameters = parseMethodParameters();

    //4)
    Type returnType = new Type(); // void stub
    if (parser.is(T.T_ARROW)) {
      parser.checkedMove(T.T_ARROW);
      if (parser.is(Keywords.void_ident)) {
        parser.move(); // void stub
      } else {
        returnType = new ParseType(parser).getType();
      }
    }

    final StmtBlock block = new ParseStatement(parser).parseBlock(VarBase.METHOD_VAR);
    final ClassMethodDeclaration method = new ClassMethodDeclaration(ClassMethodBase.IS_FUNC, clazz, ident, parameters,
        returnType, block, beginPos);

    checkMethodRedefinition(clazz, method);
    clazz.addMethod(method);
  }

  private void putDestructor(ClassDeclaration clazz) {
    final Token beginPos = parser.checkedMove(Keywords.deinit_ident);
    final StmtBlock block = new ParseStatement(parser).parseBlock(VarBase.METHOD_VAR);
    final ClassMethodDeclaration destructor = new ClassMethodDeclaration(clazz, block, beginPos);

    checkDestructorRedefinition(clazz);
    clazz.setDestructor(destructor);
  }

  private void putConstructor(ClassDeclaration clazz) {
    final Token beginPos = parser.checkedMove(Keywords.init_ident);
    final List<MethodParameter> parameters = parseMethodParameters();
    final StmtBlock block = new ParseStatement(parser).parseBlock(VarBase.METHOD_VAR);
    final Type returnType = new Type(); // the return type of constructor is 'void'
    final ClassMethodDeclaration constructor = new ClassMethodDeclaration(ClassMethodBase.IS_CONSTRUCTOR, clazz,
        Keywords.init_ident, parameters, returnType, block, beginPos);

    checkConstructorRedefinition(clazz, constructor);
    clazz.addConstructor(constructor);
  }

  private void checkDestructorRedefinition(ClassDeclaration clazz) {
    if (clazz.getDestructor() != null) {
      parser.perror("duplicate destructor");
    }
  }

  private void checkConstructorRedefinition(ClassDeclaration clazz, ClassMethodDeclaration another) {
    for (ClassMethodDeclaration constructor : clazz.getConstructors()) {
      final List<MethodParameter> fp1 = constructor.getParameters();
      final List<MethodParameter> fp2 = another.getParameters();
      if (parametersListsAreEqualByTypes(fp1, fp2)) {
        parser.perror("duplicate constructor with the same formal parameters");
      }
    }
  }

  private void checkMethodRedefinition(ClassDeclaration clazz, ClassMethodDeclaration another) {
    for (ClassMethodDeclaration method : clazz.getMethods()) {
      if (method.getIdentifier().equals(another.getIdentifier())) {
        final List<MethodParameter> fp1 = method.getParameters();
        final List<MethodParameter> fp2 = another.getParameters();
        if (parametersListsAreEqualByTypes(fp1, fp2)) {
          parser.perror("duplicate methods with the same formal parameters");
        }
      }
    }
  }

  private void checkFieldRedefinition(ClassDeclaration clazz, VarDeclarator another) {
    for (VarDeclarator field : clazz.getFields()) {
      final Ident name1 = field.getIdentifier();
      final Ident name2 = another.getIdentifier();
      if (name1.equals(name2)) {
        parser.perror("duplicate field");
      }
    }
  }

  private boolean parametersListsAreEqualByTypes(List<MethodParameter> first, List<MethodParameter> another) {
    final int bound = first.size();
    if (bound != another.size()) {
      return false;
    }
    for (int i = 0; i < bound; i++) {
      Type tp1 = first.get(i).getType();
      Type tp2 = another.get(i).getType();
      if (!tp1.is_equal_to(tp2)) {
        return false;
      }
    }
    return true;
  }

  private List<Type> parseTypeParametersT() {

    // class Thing<K, V> {
    // ...........^

    final List<Type> typenamesT = new ArrayList<>();

    if (parser.is(T.T_LT)) {
      Token open = parser.checkedMove(T.T_LT);

      typenamesT.add(new Type(parser.getIdent()));
      while (parser.is(T.T_COMMA)) {
        parser.move();
        typenamesT.add(new Type(parser.getIdent()));
      }

      Token close = parser.checkedMove(T.T_GT);
    }

    return typenamesT;
  }

  private List<MethodParameter> parseMethodParameters() {

    // func name(param: int) -> int {  }

    List<MethodParameter> parameters = new ArrayList<>();
    Token lparen = parser.lparen();

    if (parser.is(T.T_RIGHT_PAREN)) {
      Token rparen = parser.rparen();
      return parameters;
    }

    parameters.add(parseOneParam());
    while (parser.is(T.T_COMMA)) {
      Token comma = parser.moveget();
      parameters.add(parseOneParam());
    }

    Token rparen = parser.rparen();
    return parameters;
  }

  private MethodParameter parseOneParam() {
    final Token tok = parser.checkedMove(T.TOKEN_IDENT);
    final Ident id = tok.getIdent();
    final Token colon = parser.colon();
    final Type type = new ParseType(parser).getType();
    return new MethodParameter(id, type);
  }

}
