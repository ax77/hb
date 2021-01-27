package ast_parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ast_checkers.IdentRecognizer;
import ast_class.ClassDeclaration;
import ast_method.ClassMethodBase;
import ast_method.ClassMethodDeclaration;
import ast_method.MethodParameter;
import ast_method.MethodSignature;
import ast_modifiers.Modifiers;
import ast_sourceloc.SourceLocation;
import ast_stmt.StmtBlock;
import ast_symtab.IdentMap;
import ast_types.ClassType;
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

    Modifiers modifiers = new ParseModifiers(parser).parse();

    if (parser.is(IdentMap.class_ident)) {
      Token tok = parser.moveget();
      unit.put(new TypeDeclaration(parseClassDeclaration()));
      return;
    }

    // import std.list;
    if (parser.is(IdentMap.import_ident)) {
      parseImport(unit);
      return;
    }

    parser.perror("unimpl");
  }

  private void parseImport(CompilationUnit unit) throws IOException {

    Token tok = parser.checkedMove(IdentMap.import_ident);
    StringBuilder sb = new StringBuilder();

    while (!parser.isEof()) {

      boolean isOk = parser.is(T.TOKEN_IDENT) || parser.is(T.T_DOT) || parser.is(T.T_SEMI_COLON);
      if (!isOk) {
        parser.perror("expect import path like: [import package.file;]");
      }
      if (parser.is(T.T_SEMI_COLON)) {
        break;
      }
      if (parser.isEof()) {
        parser.perror("unexpected EOF");
      }

      Token importname = parser.moveget();
      if (importname.ofType(T.T_DOT)) {
        sb.append("/");
      } else {
        sb.append(importname.getValue());
      }

    }

    parser.semicolon();

    //    // TODO: normal directory for imports.
    //    // now: just like this, because it is easy to manage git repository
    //    // with these std files, and it is easy and precise to access the files
    //    // from test folder instead of the root of the app.
    //    final String dir = System.getProperty("user.dir") + "/src/test/java/njast/";
    //    final String path = dir + "/" + sb.toString();
    //
    //    FileWrapper fw = new FileWrapper(path);
    //    if (!fw.isExists()) {
    //      parser.perror("file does not exists: " + path);
    //    }
    //
    //    StringBuilder source = new StringBuilder();
    //    source.append(fw.readToString(FileReadKind.AS_IS));
    //
    //    Parse nestedParser = new ParserMain(source).initiateParse();
    //    CompilationUnit unitFromImport = nestedParser.parse();
    //
    //    for (TypeDeclaration td : unitFromImport.getTypeDeclarations()) {
    //      parser.defineClassName(td.getClassDeclaration());
    //      unit.put(td);
    //    }

  }

  private ClassDeclaration parseClassDeclaration() {

    Ident ident = parser.getIdent();

    // class Thing<T> {
    // ......^....^
    List<Type> tp = new ParseTypeParameters(parser).parse(); // maybe empty

    Token lbrace = parser.lbrace();

    // we'll register this name for type-handling
    // we can't register the whole class, because our compiler is not a one-pass one ;)
    // we'll resolve all methods, fields later, right now we need to know that this simple
    // identifier is a class name, and nothing more...
    // maybe I'm wrong here, and there is more clean and nice way, I'll think about it later.
    //

    ClassDeclaration clazz = parser.getClassType(ident);
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

    // init
    // 
    boolean isConstructorDeclaration = parser.is(IdentMap.init_ident);
    if (isConstructorDeclaration) {
      final Token tok = parser.checkedMove(IdentMap.init_ident);
      final List<MethodParameter> parameters = new ParseFormalParameterList(parser).parse();
      final StmtBlock block = new ParseStatement(parser).parseBlock(VarBase.METHOD_VAR);
      final MethodSignature signature = new MethodSignature(IdentMap.init_ident, parameters);
      final Type returnType = new Type(new ClassType(clazz, new ArrayList<>()));
      final ClassMethodDeclaration constructor = new ClassMethodDeclaration(ClassMethodBase.IS_CONSTRUCTOR, clazz,
          signature, returnType, block, tok);

      checkConstructorRedefinition(clazz, constructor);
      clazz.addConstructor(constructor);
      return;
    }

    // deinit
    //
    boolean isDestructor = parser.is(IdentMap.deinit_ident);
    if (isDestructor) {
      final Token tok = parser.checkedMove(IdentMap.deinit_ident);
      final StmtBlock block = new ParseStatement(parser).parseBlock(VarBase.METHOD_VAR);
      final ClassMethodDeclaration destructor = new ClassMethodDeclaration(clazz, block, tok);

      checkDestructorRedefinition(clazz);
      clazz.setDestructor(destructor);
      return;
    }

    // function
    //
    boolean isFunction = parser.is(IdentMap.func_ident);
    if (isFunction) {
      ClassMethodDeclaration methodDeclaration = new ParseMethodDeclaration(parser).parse(clazz);
      checkMethodRedefinition(clazz, methodDeclaration);
      clazz.addMethod(methodDeclaration);
      return;
    }

    // var
    // let
    // weak var
    // private var
    // ...

    boolean isCorrectVarBegin = IdentRecognizer.is_any_modifier(parser.tok()) || parser.is(IdentMap.var_ident)
        || parser.is(IdentMap.let_ident);
    if (!isCorrectVarBegin) {
      parser.perror("expect variable - declaration");
    }

    VarDeclarator field = new ParseVarDeclarator(parser).parse(VarBase.CLASS_FIELD);
    field.setClazz(clazz);
    checkFieldRedefinition(clazz, field);
    clazz.addField(field);

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

}
