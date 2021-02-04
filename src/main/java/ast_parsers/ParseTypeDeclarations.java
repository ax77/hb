package ast_parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_st2_annotate.Mods;
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

public class ParseTypeDeclarations {
  private final Parse parser;

  public ParseTypeDeclarations(Parse parser) {
    this.parser = parser;
  }

  public void parse(CompilationUnit unit) throws IOException {

    // opt
    final Modifiers modifiers = new ParseModifiers(parser).parse();

    if (parser.is(Keywords.class_ident)) {
      final ClassDeclaration clazz = parseClassDeclaration();
      if (clazz.isTemplate()) {
        unit.putTemplate(clazz);
      } else {
        unit.putClazz(clazz);
      }
    }

    else if (parser.is(Keywords.import_ident)) {
      // PackageNameCutter.cutPackageName(parser, Keywords.import_ident);
      parser.perror("import is unimpl.");
    }

    else {
      parser.perror("unimpl");
    }
  }

  private ClassDeclaration parseClassDeclaration() {

    parser.checkedMove(Keywords.class_ident);
    final Token beginPos = parser.checkedMove(T.TOKEN_IDENT);
    final Ident ident = beginPos.getIdent();

    // class Thing<T> {
    // ......^....^
    final List<Type> typeParameters = parseTypeParametersT();
    parser.lbrace();

    // it may be a previously fully-parsed class, or a new one.
    ClassDeclaration clazz = null;
    if (parser.isClassName(ident)) {
      clazz = parser.getClassType(ident);
    }
    if (clazz == null) {
      clazz = new ClassDeclaration(ident, typeParameters, beginPos);
    }

    parser.defineClassName(clazz);
    parser.setCurrentClass(clazz);

    // class C { }
    // ..........^
    if (parser.is(T.T_RIGHT_BRACE)) {
      parser.rbrace();
      parser.setCurrentClass(null);

      clazz.setComplete(true);
      return clazz;
    }

    while (!parser.isEof()) {
      putConstructorOrFieldOrMethodIntoClass(clazz);
      if (parser.is(T.T_RIGHT_BRACE)) {
        break;
      }
    }

    parser.rbrace();
    parser.setCurrentClass(null);

    clazz.setComplete(true);
    return clazz;
  }

  private void putConstructorOrFieldOrMethodIntoClass(ClassDeclaration clazz) {

    final Modifiers modifiers = new ParseModifiers(parser).parse();

    if (parser.is(Keywords.init_ident)) {
      putConstructor(clazz, modifiers);
    } else if (parser.is(Keywords.deinit_ident)) {
      putDestructor(clazz, modifiers);
    } else if (parser.is(Keywords.func_ident)) {
      putMethod(clazz, modifiers);
    } else {
      putField(clazz, modifiers);
    }
  }

  private void putField(ClassDeclaration clazz, Modifiers modifiers) {

    if (!Mods.isCorrectFieldMods(modifiers)) {
      parser.perror("field modifiers are incorrect: " + modifiers.toString());
    }

    final VarDeclarator field = new ParseVarDeclarator(parser).parse(VarBase.CLASS_FIELD, modifiers);
    field.setClazz(clazz);

    checkFieldRedefinition(clazz, field);
    clazz.addField(field);

  }

  private void putMethod(ClassDeclaration clazz, Modifiers modifiers) {

    if (!Mods.isCorrectMethodMods(modifiers)) {
      parser.perror("method modifiers are incorrect: " + modifiers.toString());
    }

    final Token beginPos = parser.checkedMove(Keywords.func_ident);
    final Ident ident = parser.getIdent();
    final List<VarDeclarator> parameters = parseMethodParameters();

    //4)
    Type returnType = new Type(parser.tok()); // void stub
    if (parser.is(T.T_ARROW)) {
      parser.checkedMove(T.T_ARROW);
      if (parser.is(Keywords.void_ident)) {
        parser.move(); // void stub
      } else {
        returnType = new ParseType(parser).getType();
      }
    }

    final StmtBlock block = new ParseStatement(parser).parseBlock(VarBase.METHOD_VAR);
    final ClassMethodDeclaration method = new ClassMethodDeclaration(ClassMethodBase.IS_FUNC, modifiers, clazz, ident,
        parameters, returnType, block, beginPos);

    checkMethodRedefinition(clazz, method);
    clazz.addMethod(method);
  }

  private void putDestructor(ClassDeclaration clazz, Modifiers modifiers) {
    if (!modifiers.isEmpty()) {
      parser.perror("destructor with modifiers: " + modifiers.toString());
    }

    final Token beginPos = parser.checkedMove(Keywords.deinit_ident);
    final StmtBlock block = new ParseStatement(parser).parseBlock(VarBase.METHOD_VAR);
    final ClassMethodDeclaration destructor = new ClassMethodDeclaration(clazz, block, beginPos);

    checkDestructorRedefinition(clazz);
    clazz.setDestructor(destructor);
  }

  private void putConstructor(ClassDeclaration clazz, Modifiers modifiers) {

    if (!Mods.isCorrectConstructorMods(modifiers)) {
      parser.perror("constructor modifiers are incorrect: " + modifiers.toString());
    }

    final Token beginPos = parser.checkedMove(Keywords.init_ident);
    final List<VarDeclarator> parameters = parseMethodParameters();
    final StmtBlock block = new ParseStatement(parser).parseBlock(VarBase.METHOD_VAR);
    final Type returnType = new Type(beginPos); // the return type of constructor is 'void'
    final ClassMethodDeclaration constructor = new ClassMethodDeclaration(ClassMethodBase.IS_CONSTRUCTOR, modifiers,
        clazz, Keywords.init_ident, parameters, returnType, block, beginPos);

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
    return new Type(tok.getIdent(), tok);
  }

  private List<VarDeclarator> parseMethodParameters() {

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
    final Modifiers mods = Mods.letMods();
    final Token tok = parser.checkedMove(T.TOKEN_IDENT);
    final Ident id = tok.getIdent();
    parser.colon();
    final Type type = new ParseType(parser).getType();
    return new VarDeclarator(VarBase.METHOD_PARAMETER, mods, type, id, tok);
  }

}