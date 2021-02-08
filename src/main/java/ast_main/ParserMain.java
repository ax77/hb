package ast_main;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_st0_resolve.CUnitCompleteChecker;
import ast_st1_templates.InstatantiationUnitBuilder;
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

  public ParserMain(String filename) {
    NullChecker.check(filename);
    this.filename = filename;
  }

  @Override
  public Parse initiateParse() throws IOException {
    initIdents();

    // final FileWrapper fw = new FileWrapper(filename);
    // fw.assertIsExists();
    // fw.assertIsFile();
    // 
    // final Stream s = new Stream(filename, fw.readToString(FileReadKind.APPEND_LF));
    // return new Parse(new Tokenlist(s.getTokenlist()));

    final UnitInfo info = new UnitInfo(filename);
    final List<Token> tokens = info.getTokenlist();
    final Parse parser = new Parse(new Tokenlist(tokens));

    for (ClassDeclaration c : info.getTypenames()) {
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
    return unitBuilder.getInstantiationUnit();
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
