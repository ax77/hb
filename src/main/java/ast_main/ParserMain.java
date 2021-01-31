package ast_main;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_st1_templates.InstatantiationUnitBuilder;
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

  public ParserMain(String filename) {
    NullChecker.check(filename);
    this.filename = filename;
  }

  @Override
  public Parse initiateParse() throws IOException {
    initIdents();

    // final UnitInfo info = new UnitInfo(filename);
    // final List<Token> tokens = info.getTokenlist();
    // 
    // // for sure!
    // if (!tokens.isEmpty()) {
    //   Token last = tokens.get(tokens.size() - 1);
    //   if (!last.ofType(T.TOKEN_EOF)) {
    //     throw new AstParseException("token-list without EOF");
    //   }
    // }

    FileWrapper fw = new FileWrapper(filename);
    fw.assertIsExists();
    fw.assertIsFile();
    Stream s = new Stream(filename, fw.readToString(FileReadKind.APPEND_LF));

    final List<Token> tokenlist = s.getTokenlist();
    final Parse parser = new Parse(new Tokenlist(tokenlist));

    // for (Entry<String, String> e : info.getClassLocations().entrySet()) {
    //   final Ident className = Hash_ident.getHashedIdent(e.getKey());
    //   final ClassDeclaration clazz = new ClassDeclaration(className);
    //   parser.defineClassName(clazz);
    // }

    return parser;
  }

  @Override
  public CompilationUnit parseCompilationUnit() throws IOException {
    Parse parser = initiateParse();
    final CompilationUnit result = parser.parse();

    return result;
  }

  @Override
  public InstantiationUnit parseInstantiationUnit() throws IOException {
    final CompilationUnit unit = parseCompilationUnit();
    checkAllClassesAreComplete(unit);
    unit.sort();

    final InstatantiationUnitBuilder unitBuilder = new InstatantiationUnitBuilder(unit);
    final InstantiationUnit result = unitBuilder.getInstantiationUnit();

    return result;
  }

  private void checkAllClassesAreComplete(final CompilationUnit unit) {
    for (ClassDeclaration clazz : unit.getClasses()) {
      if (!clazz.isComplete()) {
        errorIncomplete(clazz, "incomplete class");
      }
    }
    for (ClassDeclaration clazz : unit.getTemplates()) {
      if (!clazz.isComplete()) {
        errorIncomplete(clazz, "incomplete template");
      }
    }
    for (ClassDeclaration clazz : unit.getForwards()) {
      if (!clazz.isComplete()) {
        errorIncomplete(clazz, "unused forward declaration");
      }
    }
  }

  private void errorIncomplete(ClassDeclaration clazz, String msg) {
    throw new AstParseException(clazz.getLocationToString() + ": error: " + msg + ":" + clazz.getIdentifier());
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
