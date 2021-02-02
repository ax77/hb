package ast_main;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_st1_templates.InstatantiationUnitBuilder;
import ast_st1_templates.TypeSetter;
import ast_symtab.Keywords;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeBase;
import ast_types.TypeUnresolvedId;
import ast_unit.CompilationUnit;
import ast_unit.InstantiationUnit;
import errors.AstParseException;
import hashed.Hash_ident;
import parse.Parse;
import parse.Tokenlist;
import tokenize.Ident;
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

  private void bind(CompilationUnit tu) {
    final List<ClassDeclaration> classes = tu.getClasses();
    for (ClassDeclaration c : classes) {
      List<TypeSetter> typeSetters = c.getTypeSetters();
      for (TypeSetter ts : typeSetters) {
        final Type tp = ts.getType();
        if (tp.is(TypeBase.TP_UNRESOLVED_ID)) {
          final TypeUnresolvedId unresolved = tp.getUnresolvedId();
          final ClassDeclaration realtype = find(unresolved, classes);
          if (realtype == null) {
            throw new AstParseException("cannot find and bind class: " + unresolved.getTypeName().toString());
          }
          ts.setType(new Type(new ClassTypeRef(realtype, new ArrayList<>())));
        }
      }
    }
  }

  private ClassDeclaration find(TypeUnresolvedId unresolved, List<ClassDeclaration> classes) {
    final Ident typeName = unresolved.getTypeName();
    for (ClassDeclaration c : classes) {
      if (typeName.equals(c.getIdentifier())) {
        return c;
      }
    }
    return null;

  }

  @Override
  public CompilationUnit parseCompilationUnit() throws IOException {
    Parse parser = initiateParse();
    final CompilationUnit result = parser.parse();

    bind(result);
    return result;
  }

  @Override
  public InstantiationUnit parseInstantiationUnit() throws IOException {
    final CompilationUnit unit = parseCompilationUnit();
    unit.sort();

    final InstatantiationUnitBuilder unitBuilder = new InstatantiationUnitBuilder(unit);
    final InstantiationUnit result = unitBuilder.getInstantiationUnit();

    return result;
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
