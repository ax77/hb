package ast_main;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import _st0_resolve.CUnitCompleteChecker;
import _st1_templates.InstatantiationUnitBuilder;
import _st3_linearize_expr.BuiltinsFnSet;
import ast_class.ClassDeclaration;
import ast_symtab.Keywords;
import ast_unit.CompilationUnit;
import ast_unit.InstantiationUnit;
import errors.AstParseException;
import hashed.Hash_ident;
import parse.Parse;
import parse.Tokenlist;
import tokenize.Stream;
import tokenize.Token;
import utils_fio.FileReadKind;
import utils_fio.FileWrapper;
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

  @Override
  public Parse initiateParse() throws IOException {
    initIdents();
    BuiltinsFnSet.clear();

    //// without any imports it is looks like this:
    ////
    // final FileWrapper fw = new FileWrapper(filename);
    // fw.assertIsExists();
    // fw.assertIsFile();
    // 
    // final Stream s = new Stream(filename, fw.readToString(FileReadKind.APPEND_LF));
    // return new Parse(new Tokenlist(s.getTokenlist()));

    if (isFromFile) {
      final UnitInfo info = new UnitInfo(filename);
      final List<Token> tokens = info.getTokenlist();
      final Parse parser = new Parse(new Tokenlist(tokens));

      for (ClassDeclaration c : info.getTypenames()) {
        parser.defineClassName(c);
      }

      return parser;
    }

    /// this is only for unit-testing, because sometimes
    /// it is much easy to test the source from string
    /// instead of the file
    final String dir = System.getProperty("user.dir");
    final StringBuilder predef = new StringBuilder();
    predef.append(new FileWrapper(dir + "/std/array.hb").readToString(FileReadKind.APPEND_LF));

    final String sourceGiven = predef.toString() + "\n" + sb.toString();
    final Stream s = new Stream("<string-source>", sourceGiven);
    final List<Token> tokenlist = s.getTokenlist();
    final Parse parser = new Parse(new Tokenlist(tokenlist));

    for (ClassDeclaration c : new TypenamesFinder(tokenlist).getTypenames()) {
      parser.defineClassName(c);
    }

    return parser;

  }

  @Override
  public CompilationUnit parseCompilationUnit() throws IOException {
    Parse parser = initiateParse();

    final CompilationUnit result = parser.parse();
    CUnitCompleteChecker.checkAllClassesAreComplete(result);

    return result;
  }

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
