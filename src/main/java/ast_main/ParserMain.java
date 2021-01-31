package ast_main;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map.Entry;

import ast_class.ClassDeclaration;
import ast_st1_templates.InstatantiationUnitBuilder;
import ast_symtab.Keywords;
import ast_unit.CompilationUnit;
import ast_unit.InstantiationUnit;
import errors.AstParseException;
import hashed.Hash_ident;
import parse.Parse;
import parse.Tokenlist;
import tokenize.Ident;
import tokenize.T;
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

    final UnitInfo info = new UnitInfo(filename);
    final List<Token> tokens = info.getTokenlist();

    // for sure!
    if (!tokens.isEmpty()) {
      Token last = tokens.get(tokens.size() - 1);
      if (!last.ofType(T.TOKEN_EOF)) {
        throw new AstParseException("token-list without EOF");
      }
    }

    final Parse parser = new Parse(new Tokenlist(tokens));

    for (Entry<String, String> e : info.getClassLocations().entrySet()) {
      final Ident className = Hash_ident.getHashedIdent(e.getKey());
      final ClassDeclaration clazz = new ClassDeclaration(className);
      parser.defineClassName(clazz);
    }

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
    for (ClassDeclaration clazz : unit.getClasses()) {
      if (!clazz.isComplete()) {
        throw new AstParseException("incomplete class: " + clazz.getIdentifier());
      }
    }

    final InstatantiationUnitBuilder unitBuilder = new InstatantiationUnitBuilder(unit);
    final InstantiationUnit result = unitBuilder.getInstantiationUnit();

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
