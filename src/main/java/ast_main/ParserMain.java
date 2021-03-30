package ast_main;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.UUID;

import _st0_resolve.CUnitCompleteChecker;
import _st1_templates.InstatantiationUnitBuilder;
import _st3_linearize_expr.BuiltinsFnSet;
import ast_class.ClassDeclaration;
import ast_main.imports.GlobalSymtab;
import ast_symtab.Keywords;
import ast_types.Type;
import ast_unit.CompilationUnit;
import ast_unit.InstantiationUnit;
import errors.AstParseException;
import hashed.Hash_ident;
import parse.Parse;
import parse.Tokenlist;
import tokenize.Stream;
import utils_fio.FileReadKind;
import utils_fio.FileWrapper;
import utils_oth.NullChecker;
import utils_ser.SerializationUtils;

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

  @Override
  public Parse initiateParse() throws IOException {
    initIdents();
    BuiltinsFnSet.clear();
    GlobalSymtab.clear();

    //// without any imports it is looks like this:
    ////
    // final FileWrapper fw = new FileWrapper(filename);
    // fw.assertIsExists();
    // fw.assertIsFile();
    // 
    // final Stream s = new Stream(filename, fw.readToString(FileReadKind.APPEND_LF));
    // return new Parse(new Tokenlist(s.getTokenlist()));

    if (isFromFile) {
      final Stream s = new Stream(filename, new FileWrapper(filename).readToString(FileReadKind.APPEND_LF));
      return new Parse(new Tokenlist(s.getTokenlist()));
    }

    final Stream s = new Stream("<string-source>", sb.toString());
    return new Parse(new Tokenlist(s.getTokenlist()));

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

    // TODO:
    // if (mainClass.getMethods().size() != 1) {
    //   throw new AstParseException("main class should contain main-method");
    // }
    // ClassMethodDeclaration mainMethod = mainClass.getMethods().get(0);
    // if (!mainMethod.getIdentifier().getName().equals("main")) {
    //   throw new AstParseException("main method not found");
    // }
    // if (!mainMethod.getType().is_void()) {
    //   throw new AstParseException("main method should return the void");
    // }

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
