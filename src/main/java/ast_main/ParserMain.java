package ast_main;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import _st0_resolve.CUnitCompleteChecker;
import _st1_templates.InstatantiationUnitBuilder;
import _st3_linearize_expr.BuiltinsFnSet;
import ast_class.ClassDeclaration;
import ast_main.imports.GlobalSymtab;
import ast_parsers.ParsePackageName;
import ast_symtab.Keywords;
import ast_unit.CompilationUnit;
import ast_unit.InstantiationUnit;
import errors.AstParseException;
import hashed.Hash_ident;
import parse.Parse;
import parse.Tokenlist;
import tokenize.Env;
import tokenize.Stream;
import tokenize.Token;
import utils_fio.FileReadKind;
import utils_fio.FileWrapper;
import utils_oth.Normalizer;
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

  ///// IMPORTS AGAIN!!!! SH!!!!!

  private String getFullFilenameFromImport(String input) {
    if (!input.contains("::")) {
      throw new AstParseException("import name is incorrect: " + input);
    }

    final String dir = System.getProperty("user.dir");
    String found = input.substring(0, input.lastIndexOf("::")).trim();

    found = dir + "/" + found.replace('.', '/');
    found = Normalizer.normalize(found);

    return found.trim() + ".hb";
  }

  class ImportsFound {
    final Set<String> result;
    final Set<String> importsNames;

    public ImportsFound() {
      this.result = new HashSet<>();
      this.importsNames = new HashSet<>();
    }

    public Set<String> getResult() {
      return result;
    }

    public Set<String> getImportsNames() {
      return importsNames;
    }

    public void addFullpath(String fullpath) {
      this.result.add(fullpath);
    }

    public void addImportName(String name) {
      this.importsNames.add(name);
    }

  }

  private ImportsFound prepareImportsDirty(String firstFile) throws IOException {

    final ImportsFound result = new ImportsFound();

    // without normalizations there may be possible
    // duplication if the path was given with straight or reverse slash /\
    // main/folder/file.hb and main\folder\file.hb
    firstFile = Normalizer.normalize(firstFile).trim();
    result.addFullpath(firstFile); // it's important not to forget this very file :)

    final LinkedList<String> stack = new LinkedList<String>();
    stack.add(firstFile);

    // imports we've already parsed
    final Set<String> processed = new HashSet<>();

    while (!stack.isEmpty()) {
      String curfile = stack.removeFirst();

      // we've already been read the full content
      // of the given file, parse that file, and 
      // extract all the imports from it.
      // it may happen - if we imported the same file
      // from many different places, a common practice.
      // for example: you import array.hb file 100 times per-project :)
      if (processed.contains(curfile)) {
        continue;
      }
      processed.add(curfile);

      final String content = new FileWrapper(curfile).readToString(FileReadKind.APPEND_LF);
      final Stream stream = new Stream(curfile, content);
      final Parse parser = new Parse(new Tokenlist(stream.getTokenlist()));
      final Set<String> importsFromTheFile = new HashSet<>();

      while (!parser.isEof()) {
        if (parser.is(Keywords.import_ident)) {
          String importname = ParsePackageName.parse(parser, Keywords.import_ident);
          importsFromTheFile.add(importname);
        } else {
          parser.move();
        }
      }

      for (String originalImportName : importsFromTheFile) {
        String fullpath = getFullFilenameFromImport(originalImportName);
        stack.addLast(fullpath);

        result.addFullpath(fullpath);
        result.addImportName(originalImportName);
      }

    }

    return result;

  }

  private void removeEOF(List<Token> tokenlist) {
    if (!tokenlist.isEmpty()) {
      final int lastIdx = tokenlist.size() - 1;
      if (tokenlist.get(lastIdx).typeIsSpecialStreamMarks()) {
        tokenlist.remove(lastIdx);
      }
    }
  }

  private List<Token> prepareTypesDirty(String path) throws IOException {

    final ImportsFound result = prepareImportsDirty(path);

    StringBuilder sb = new StringBuilder();
    for (String s : result.getImportsNames()) {
      sb.append("import ");
      sb.append(s);
      sb.append(";\n");
    }

    List<Token> tokens = new ArrayList<Token>();

    final List<Token> forwardImportTokens = new Stream("builtin", sb.toString()).getTokenlist();
    removeEOF(forwardImportTokens);
    tokens.addAll(forwardImportTokens);

    for (String s : result.getResult()) {
      String content = new FileWrapper(s).readToString(FileReadKind.APPEND_LF).trim();
      final Stream stream = new Stream(s, content);

      final List<Token> tokenlist = stream.getTokenlist();
      removeEOF(tokenlist);
      tokens.addAll(tokenlist);
    }

    tokens.add(Env.EOF_TOKEN_ENTRY);
    return tokens;
  }

  /////////////

  @Override
  public Parse initiateParse() throws IOException {
    clearAllHashedTemps();
    initIdents();

    // let's do this again in a dirty way: like C-preprocessor pasting...
    // idn how to solve this properly now, but I need these imports...
    // it works really weird, because our source-location is LOST, etc.
    // but it works fine by now.
    final List<Token> tokens = prepareTypesDirty(filename);

    for (Token tok : tokens) {
      if (tok.hasLeadingWhitespace()) {
        System.out.printf("%s", " ");
      }
      System.out.printf("%s", tok.getValue());
      if (tok.isNewLine()) {
        System.out.println();
      }
    }

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
