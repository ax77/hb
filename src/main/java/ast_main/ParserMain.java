package ast_main;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import _st0_resolve.CUnitCompleteChecker;
import _st1_templates.InstatantiationUnitBuilder;
import _st3_linearize_expr.BuiltinsFnSet;
import ast_class.ClassDeclaration;
import ast_main.imports.GlobalSymtab;
import ast_main.imports.ImportsPreparer;
import ast_symtab.Keywords;
import ast_unit.CompilationUnit;
import ast_unit.InstantiationUnit;
import errors.AstParseException;
import hashed.Hash_ident;
import parse.Parse;
import parse.Tokenlist;
import tokenize.Token;
import utils_oth.NullChecker;

public class ParserMain implements ParserMainApi {

  private final String filename;
  private final StringBuilder sb;
  private final boolean isFromFile;

  public ParserMain(String filename) {
    NullChecker.check(filename);

    this.filename = filename;
    this.sb = null;
    this.isFromFile = true;
  }

  public ParserMain(StringBuilder sb) {
    NullChecker.check(sb);

    this.filename = null;
    this.sb = sb;
    this.isFromFile = false;
  }

  private void clearAllHashedTemps() {
    Hash_ident.clear();
    BuiltinsFnSet.clear();
    GlobalSymtab.clear();
  }

  @Override
  public Parse initiateParse() throws IOException {
    clearAllHashedTemps();
    initIdents();

    // let's do this again in a dirty way: like C-preprocessor pasting...
    // idn how to solve this properly now, but I need these imports...
    // it works really weird, because our source-location is LOST, etc.
    // but it works fine by now.
    final List<Token> tokens = ImportsPreparer.findAllClassesAndDefineTheirHeaders(filename);
    return new Parse(new Tokenlist(tokens));

  }

  @Override
  public CompilationUnit parseCompilationUnit() throws IOException {
    Parse parser = initiateParse();

    final CompilationUnit result = parser.parse();
    CUnitCompleteChecker.checkAllClassesAreComplete(result);
    expandInterfaces(result);

    return result;
  }

  /// interfaces:+
  private void expandInterfaces(CompilationUnit result) {
    for (ClassDeclaration c : result.getClasses()) {
      if (!c.isInterface()) {
        continue;
      }
      for (ClassDeclaration impl : c.getImplementations()) {
      }
    }
  }
  /// interfaces:-

  @Override
  public InstantiationUnit parseInstantiationUnit() throws IOException {
    final CompilationUnit unit = parseCompilationUnit();
    final InstatantiationUnitBuilder unitBuilder = new InstatantiationUnitBuilder(unit);
    final InstantiationUnit result = unitBuilder.getInstantiationUnit();

    ClassDeclaration mainClass = result.getClassByName("main_class");
    if (mainClass == null) {
      throw new AstParseException("main class not found");
    }

    return result;
  }

  // we have to initialize it here because of the static-laziness
  // we must be sure that all keywords will be created before parser will be run.
  //
  private void initIdents() {

    for (Field field : Keywords.class.getDeclaredFields()) {
      final String fname = field.getName();
      if (!fname.endsWith("_ident")) {
        throw new AstParseException("expect ident name");
      }
      final int pos = fname.indexOf("_ident");
      final String sub = fname.substring(0, pos).trim();
      Hash_ident.getHashedIdent(sub, 32);
    }
  }

}
