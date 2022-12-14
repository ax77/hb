package ast_parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_class.InterfaceItem;
import ast_main.imports.GlobalSymtab;
import ast_method.ClassMethodBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_modifiers.ModifiersChecker;
import ast_stmt.StmtBlock;
import ast_symtab.Keywords;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_unit.CompilationUnit;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import parse.Parse;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

public class ParseTypeDeclarations {
  private final Parse parser;

  public ParseTypeDeclarations(Parse parser) {
    this.parser = parser;
  }

  public void parse(CompilationUnit unit) throws IOException {

    // opt
    @SuppressWarnings("unused")
    final Modifiers modifiers = new ParseModifiers(parser).parse();

    if (parser.is(Keywords.class_ident) || parser.is(Keywords.interface_ident)) {
      final ClassDeclaration clazz = parseClassDeclaration(parser.tok().getIdent());

      if (clazz.isTemplate()) {
        unit.putTemplate(clazz);
      } else {
        unit.putClazz(clazz);
      }
    }

    else if (parser.is(Keywords.import_ident)) {
      @SuppressWarnings("unused")
      String importName = ParsePackageName.parse(parser, Keywords.import_ident);
    }

    else if (parser.is(Keywords.enum_ident)) {
      final ClassDeclaration clazz = parseEnum(parser.tok().getIdent());
      unit.putClazz(clazz);
    }

    else {
      parser.perror("unimpl");
    }

  }

  private ClassDeclaration parseEnum(Ident keyword) {
    parser.checkedMove(keyword);
    final Token beginPos = parser.checkedMove(T.TOKEN_IDENT);
    final Ident ident = beginPos.getIdent();

    // It MUST be there
    ClassDeclaration clazz = GlobalSymtab.getClassType(parser, ident);
    parser.setCurrentClass(clazz);
    final Type type = new Type(new ClassTypeRef(clazz, clazz.getTypeParametersT()));

    parser.lbrace();

    clazz.addField(getOneEnumerator(clazz, type));
    while (parser.is(T.T_COMMA)) {
      parser.move();
      clazz.addField(getOneEnumerator(clazz, type));
    }

    parser.rbrace();
    parser.setCurrentClass(null);

    clazz.setComplete(true);
    return clazz;
  }

  private VarDeclarator getOneEnumerator(ClassDeclaration clazz, final Type type) {
    final Token pos = parser.tok();
    VarDeclarator var = new VarDeclarator(VarBase.CLASS_FIELD, new Modifiers(), type, parser.getIdent(), pos);
    var.setClazz(clazz);
    return var;
  }

  private ClassDeclaration parseClassDeclaration(Ident keyword) {

    parser.checkedMove(keyword);
    final Token beginPos = parser.checkedMove(T.TOKEN_IDENT);
    final Ident ident = beginPos.getIdent();

    // class Thing<T> {
    // ......^....^
    @SuppressWarnings("unused")
    final List<Type> typeParameters = new ParseTypeParameters(parser).parse();

    // It MUST be there
    ClassDeclaration clazz = GlobalSymtab.getClassType(parser, ident);
    parser.setCurrentClass(clazz);

    // implements list for a class
    if (keyword.equals(Keywords.class_ident)) {
      if (parser.is(Keywords.implements_ident)) {
        parser.move();
        putOneInterface(clazz);
        while (parser.is(T.T_COMMA)) {
          parser.move();
          putOneInterface(clazz);
        }
      }
    }

    parser.lbrace();

    // class C { }
    // ..........^
    if (parser.is(T.T_RIGHT_BRACE)) {
      parser.rbrace();
      parser.setCurrentClass(null);

      clazz.setComplete(true);
      return clazz;
    }

    if (keyword.equals(Keywords.class_ident)) {
      while (!parser.isEof()) {
        putConstructorOrFieldOrMethodIntoClass(clazz);
        if (parser.is(T.T_RIGHT_BRACE)) {
          break;
        }
      }
    }

    else if (keyword.equals(Keywords.interface_ident)) {
      while (!parser.isEof()) {
        parseInterfaceMethods(clazz);
        if (parser.is(T.T_RIGHT_BRACE)) {
          break;
        }
      }
    }

    else {
      parser.perror("type-name is not recognized");
    }

    parser.rbrace();
    parser.setCurrentClass(null);

    clazz.setComplete(true);
    return clazz;
  }

  private void putOneInterface(ClassDeclaration clazz) {
    final Type tp = new ParseType(parser).getType();
    tp.getClassTypeFromRef().addImpl(clazz);
    final InterfaceItem item = new InterfaceItem(tp);
    clazz.addInterfaceToImplements(item);
  }

  private void parseInterfaceMethods(ClassDeclaration clazz) {

    final Modifiers mods = new ParseModifiers(parser).parse();
    final Type type = getTypeOrVoidStub();
    final Token beginPos = parser.checkedMove(T.TOKEN_IDENT);
    final Ident name = beginPos.getIdent();

    putMethod(clazz, mods, type, name, beginPos, true);
    parser.semicolon();

  }

  private Type getTypeOrVoidStub() {
    Type type = new Type(parser.tok());
    if (parser.is(Keywords.void_ident)) {
      parser.move();
    } else {
      type = new ParseType(parser).getType();
    }
    return type;
  }

  private boolean isConstructor(ClassDeclaration clazz) {
    if (!parser.isUserDefinedIdentNoKeyword(parser.tok())) {
      return false;
    }
    final boolean idTheSame = parser.tok().getIdent().equals(clazz.getIdentifier());
    return idTheSame && parser.peek().ofType(T.T_LEFT_PAREN);
  }

  private void putConstructorOrFieldOrMethodIntoClass(ClassDeclaration clazz) {

    final Modifiers mods = new ParseModifiers(parser).parse();

    if (isConstructor(clazz)) {
      putConstructor(clazz, mods);
      return;
    }

    if (parser.is(T.T_TILDE)) {
      putDestructor(clazz, mods);
      return;
    }

    if (parser.is(Keywords.test_ident)) {
      putTestMethod(clazz, mods);
      return;
    }

    // field, or method, nothing else.
    // private int a = 0;
    // private int a ;
    // private int a() {}

    final Type type = getTypeOrVoidStub();
    final Token beginPos = parser.checkedMove(T.TOKEN_IDENT);
    final Ident name = beginPos.getIdent();

    if (type.isVoid() || parser.is(T.T_LEFT_PAREN)) {
      putMethod(clazz, mods, type, name, beginPos, false);
      return;
    }

    if (parser.is(T.T_ASSIGN) || parser.is(T.T_SEMI_COLON)) {
      putField(clazz, mods, type, name, beginPos);
      return;
    }

    parser.perror("class-member is not recognized");

  }

  private void putTestMethod(ClassDeclaration clazz, Modifiers modifiers) {
    if (!modifiers.isEmpty()) {
      parser.perror("test-method with modifiers: " + modifiers.toString());
    }

    final Token beginPos = parser.checkedMove(Keywords.test_ident);
    final Token testName = parser.checkedMove(T.TOKEN_STRING);

    StmtBlock block = new ParseStatement(parser).parseBlock(VarBase.METHOD_VAR);
    final ClassMethodDeclaration testMethod = new ClassMethodDeclaration(clazz, testName.getValue(), block, beginPos);

    checkTestMethodRedefinitionByName(testMethod, clazz);
    clazz.addTestMethod(testMethod);
  }

  private void checkTestMethodRedefinitionByName(ClassMethodDeclaration testMethod, ClassDeclaration clazz) {
    for (ClassMethodDeclaration m : clazz.getTests()) {
      if (m.getTestName().equals(testMethod.getTestName())) {
        parser.perror("test-method name redefined: " + m.getTestName());
      }
    }
  }

  private void putDestructor(ClassDeclaration clazz, Modifiers modifiers) {
    if (!modifiers.isEmpty() && !clazz.isNativeArray() && !clazz.isNativeString()) {
      parser.perror("destructor with modifiers: " + modifiers.toString());
    }

    final Token beginPos = parser.checkedMove(T.T_TILDE);
    Ident id = parser.getIdent();
    if (!id.equals(clazz.getIdentifier())) {
      parser.perror("destructor must have the name of its class.");
    }
    parser.lparen();
    parser.rparen();

    StmtBlock block = new StmtBlock();
    if (!modifiers.isNativeOnly()) {
      block = new ParseStatement(parser).parseBlock(VarBase.METHOD_VAR);
    } else {
      parser.semicolon();
    }

    final ClassMethodDeclaration destructor = new ClassMethodDeclaration(modifiers, clazz, block, beginPos);

    if (clazz.getDestructor() != null) {
      parser.perror("duplicate destructor");
    }
    clazz.setDestructor(destructor);
  }

  private void putField(ClassDeclaration clazz, Modifiers modifiers, Type type, Ident name, Token beginPos) {

    if (!ModifiersChecker.isCorrectFieldMods(modifiers)) {
      parser.perror("field modifiers are incorrect: " + modifiers.toString());
    }

    final VarDeclarator field = new ParseVarDeclarator(parser).parse(VarBase.CLASS_FIELD, modifiers, type, name,
        beginPos);
    field.setClazz(clazz);

    checkFieldRedefinition(clazz, field);
    clazz.addField(field);

  }

  private void putMethod(ClassDeclaration clazz, Modifiers modifiers, Type type, Ident name, Token beginPos,
      boolean isInterfaceMethod) {

    if (!ModifiersChecker.isCorrectMethodMods(modifiers)) {
      parser.perror("method modifiers are incorrect: " + modifiers.toString());
    }

    final List<VarDeclarator> parameters = parseMethodParameters();

    StmtBlock block = new StmtBlock();
    if (!isInterfaceMethod) {
      if (!modifiers.isNativeOnly()) {
        block = new ParseStatement(parser).parseBlock(VarBase.METHOD_VAR);
      } else {
        parser.semicolon();
      }
    }

    final ClassMethodDeclaration method = new ClassMethodDeclaration(ClassMethodBase.IS_FUNC, modifiers, clazz, name,
        parameters, type, block, beginPos);

    checkMethodRedefinition(clazz, method);
    clazz.addMethod(method);
  }

  private void putConstructor(ClassDeclaration clazz, Modifiers modifiers) {

    if (!ModifiersChecker.isCorrectConstructorMods(modifiers)) {
      parser.perror("constructor modifiers are incorrect: " + modifiers.toString());
    }

    final Token beginPos = parser.checkedMove(T.TOKEN_IDENT);
    final List<VarDeclarator> parameters = parseMethodParameters();

    StmtBlock block = new StmtBlock();
    if (!modifiers.isNativeOnly()) {
      block = new ParseStatement(parser).parseBlock(VarBase.METHOD_VAR);
    } else {
      parser.semicolon();
    }

    final Type returnType = new Type(new ClassTypeRef(clazz, clazz.getTypeParametersT()));
    final ClassMethodDeclaration constructor = new ClassMethodDeclaration(ClassMethodBase.IS_CONSTRUCTOR, modifiers,
        clazz, clazz.getIdentifier(), parameters, returnType, block, beginPos);

    checkConstructorRedefinition(clazz, constructor);
    clazz.addConstructor(constructor);
  }

  private void checkConstructorRedefinition(ClassDeclaration clazz, ClassMethodDeclaration another) {
    for (ClassMethodDeclaration constructor : clazz.getConstructors()) {
      final List<VarDeclarator> fp1 = constructor.getParameters();
      final List<VarDeclarator> fp2 = another.getParameters();
      if (parametersListsAreEqualByTypes(fp1, fp2)) {
        parser.perror("duplicate constructor with the same formal parameters");
      }
    }
  }

  private void checkMethodRedefinition(ClassDeclaration clazz, ClassMethodDeclaration another) {
    for (ClassMethodDeclaration method : clazz.getMethods()) {
      if (method.getIdentifier().equals(another.getIdentifier())) {
        final List<VarDeclarator> fp1 = method.getParameters();
        final List<VarDeclarator> fp2 = another.getParameters();
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

  private boolean parametersListsAreEqualByTypes(List<VarDeclarator> first, List<VarDeclarator> another) {
    final int bound = first.size();
    if (bound != another.size()) {
      return false;
    }
    for (int i = 0; i < bound; i++) {
      Type tp1 = first.get(i).getType();
      Type tp2 = another.get(i).getType();
      if (!tp1.isEqualTo(tp2)) {
        return false;
      }
    }
    return true;
  }

  public List<VarDeclarator> parseMethodParameters() {

    // func name(param: int) -> int {  }

    List<VarDeclarator> parameters = new ArrayList<>();
    parser.lparen();

    if (parser.is(T.T_RIGHT_PAREN)) {
      parser.rparen();
      return parameters;
    }

    parameters.add(parseOneParam());
    while (parser.is(T.T_COMMA)) {
      parser.move();
      parameters.add(parseOneParam());
    }

    parser.rparen();
    return parameters;
  }

  private VarDeclarator parseOneParam() {
    final Modifiers mods = new ParseModifiers(parser).parse();
    final Type type = new ParseType(parser).getType();
    final Token beginPos = parser.checkedMove(T.TOKEN_IDENT);
    final Ident name = beginPos.getIdent();
    return new VarDeclarator(VarBase.METHOD_PARAMETER, mods, type, name, beginPos);
  }

}
