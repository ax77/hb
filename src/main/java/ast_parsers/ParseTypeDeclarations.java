package ast_parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_class.InterfaceItem;
import ast_method.ClassMethodBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_modifiers.ModifiersChecker;
import ast_stmt.StmtBlock;
import ast_symtab.Keywords;
import ast_types.Type;
import ast_unit.CompilationUnit;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import parse.Parse;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;
import utils_oth.NullChecker;

public class ParseTypeDeclarations {
  private final Parse parser;

  public ParseTypeDeclarations(Parse parser) {
    this.parser = parser;
  }

  public void parse(CompilationUnit unit) throws IOException {

    // opt
    final Modifiers modifiers = new ParseModifiers(parser).parse();

    if (parser.is(Keywords.class_ident) || parser.is(Keywords.interface_ident)) {
      final ClassDeclaration clazz = parseClassDeclaration(parser.tok().getIdent());
      clazz.setModifiers(modifiers);

      if (clazz.isTemplate()) {
        unit.putTemplate(clazz);
      } else {
        unit.putClazz(clazz);
      }
    }

    else if (parser.is(Keywords.import_ident)) {
      // parser.perror("import is unimplemented.");
      ParsePackageName.parse(parser, Keywords.import_ident);
    }

    else {
      parser.perror("unimpl");
    }
  }

  private ClassDeclaration parseClassDeclaration(Ident keyword) {

    parser.checkedMove(keyword);
    final Token beginPos = parser.checkedMove(T.TOKEN_IDENT);
    final Ident ident = beginPos.getIdent();

    // class Thing<T> {
    // ......^....^
    final List<Type> typeParameters = parseTypeParametersT();

    // it may be a previously fully-parsed class, or a new one.
    ClassDeclaration clazz = null;
    if (parser.isClassName(ident)) {
      // get previously defined with a 'forward' directive
      // and fill its type-parameters
      clazz = parser.getClassType(ident);
      clazz.setTypeParametersT(typeParameters);
      clazz.setBeginPos(beginPos);
    } else {
      // define a newly-founded class
      // with or without type-parameters, and WITH class body { ... }
      clazz = new ClassDeclaration(keyword, ident, typeParameters, beginPos);
      parser.defineClassName(clazz);
    }

    // set the current class immediately after we
    // found it
    NullChecker.check(clazz);
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

    if (mods.isStaticOnly() && parser.is(T.T_LEFT_BRACE)) {
      parser.unimplemented("static {  }");
      // final StmtBlock block = new ParseStatement(parser).parseBlock(VarBase.STATIC_VAR);
      // clazz.addStaticBlock(block);
      // return;
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

    final Type returnType = new Type(beginPos); // the return type of constructor is 'void'
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

  private List<Type> parseTypeParametersT() {

    // class Thing<K, V> {
    // ...........^

    final List<Type> typenamesT = new ArrayList<>();

    if (parser.is(T.T_LT)) {
      parser.lt();

      typenamesT.add(getOneTypeParameter());
      while (parser.is(T.T_COMMA)) {
        parser.move();
        typenamesT.add(getOneTypeParameter());
      }

      parser.gt();
    }

    return typenamesT;
  }

  private Type getOneTypeParameter() {
    final Token tok = parser.checkedMove(T.TOKEN_IDENT);
    return new Type(tok.getIdent());
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
