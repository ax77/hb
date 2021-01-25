package ast.parse.main;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ast.ast.nodes.unit.CompilationUnit;
import ast.parse.AstParseException;
import ast.parse.NullChecker;
import ast.parse.Parse;
import ast.symtab.IdentMap;
import jscan.fio.FileWrapper;
import jscan.hashed.Hash_all;
import jscan.hashed.Hash_ident;
import jscan.hashed.Hash_stream;
import jscan.main.Tokenlist;
import jscan.tokenize.Ident;
import jscan.tokenize.Stream;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class ParserMain implements ParserMainApi {

  private final boolean isFromFile;
  private final StringBuilder sourceFromString;
  private final String filename;

  public ParserMain(StringBuilder sourceFromString) {
    this.isFromFile = false;
    this.sourceFromString = sourceFromString;
    this.filename = "";
  }

  public ParserMain(String filename) {
    this.isFromFile = true;
    this.sourceFromString = new StringBuilder();
    this.filename = filename;
  }

  @Override
  public Tokenlist preprocess() throws IOException {

    initIdents();

    if (isFromFile) {
      ParserInternal conf = new ParserInternal(ParserInternal.PREPROCESS_FILE_INPUT, filename);
      return conf.preprocess();
    }

    ParserInternal conf = new ParserInternal(ParserInternal.PREPROCESS_STRING_INPUT, sourceFromString);
    return conf.preprocess();

  }

  @Override
  public Parse initiateParse() throws IOException {
    Tokenlist list = preprocess();

    final Parse parser = new Parse(list);
    BindAllClassTypes.bind(parser);

    return parser;
  }

  @Override
  public CompilationUnit parseUnit() throws IOException {
    Parse p = initiateParse();
    return p.parse();
  }

  private Ident g(String name) {
    return Hash_ident.getHashedIdent(name, 32);
  }

  // TODO: more clean, fast, precise...
  // we have to initialize it here because of the static-laziness
  // we must be sure that all keywords will be created before parser will be run.
  private void initIdents() {

    for (Field field : IdentMap.class.getDeclaredFields()) {
      final String fname = field.getName();
      if (!fname.endsWith("_ident")) {
        throw new AstParseException("expect ident name");
      }
      final int pos = fname.indexOf("_ident");
      final String sub = fname.substring(0, pos).trim();
      g(sub);
    }

  }

  // details.
  //
  class ParserInternal {

    public static final int PREPROCESS_STRING_INPUT = 1 << 1;
    public static final int PREPROCESS_FILE_INPUT = 1 << 2;

    private final int flag;
    private final String filename;
    private final StringBuilder source;

    public ParserInternal(int flag, String filename) {
      NullChecker.check(filename);

      this.flag = flag;
      this.filename = filename;
      this.source = new StringBuilder();

      checkFlag(PREPROCESS_FILE_INPUT);
      postInit();
    }

    public ParserInternal(int flag, StringBuilder source) {
      NullChecker.check(source);

      this.flag = flag;
      this.filename = "<unit-test>";
      this.source = source;

      checkFlag(PREPROCESS_STRING_INPUT);
      postInit();
    }

    private void checkFlag(int f) {
      if (!isFlag(f)) {
        throw new AstParseException("internal error. conf-flag incorrect.");
      }
    }

    private void postInit() {
      // XXX: file or string
      checkFlags();

      // XXX: it's important to clear all hashed entries before preprocess each translation-unit.
      Hash_all.clearAll();
    }

    public boolean isFileInput() {
      return isFlag(PREPROCESS_FILE_INPUT);
    }

    public boolean isStringInput() {
      return isFlag(PREPROCESS_STRING_INPUT);
    }

    public Tokenlist preprocess() throws IOException {

      if (isFlag(PREPROCESS_FILE_INPUT)) {
        return ppFile();
      }

      return ppString();
    }

    private void checkFlags() {
      final boolean isfile = isFlag(PREPROCESS_FILE_INPUT);
      final boolean isstring = isFlag(PREPROCESS_STRING_INPUT);
      boolean ok = isfile || isstring;
      if (!ok) {
        throw new AstParseException("need specify string|file input");
      }
      if (isfile && isstring) {
        throw new AstParseException("need specify string|file input");
      }
    }

    private Tokenlist ppString() throws IOException {
      List<Token> input = preprocessNoStrConcat(getInputInternal());
      return new Tokenlist(input);
    }

    private Tokenlist ppFile() throws IOException {
      FileWrapper fileWrapper = new FileWrapper(filename);
      fileWrapper.assertIsExists();
      fileWrapper.assertIsFile();

      List<Token> input = preprocessNoStrConcat(getInputInternal());
      return new Tokenlist(input);
    }

    private List<Token> preprocessNoStrConcat(List<Token> input) throws IOException {
      List<Token> clean = new ArrayList<Token>(0);
      for (Token t : input) {
        clean.add(t);
        if (t.ofType(T.TOKEN_EOF)) {
          break;
        }
      }
      return clean;
    }

    /// predefined? + (string | file)
    private List<Token> getInputInternal() throws IOException {
      List<Token> result = new ArrayList<Token>();

      if (isFlag(PREPROCESS_STRING_INPUT)) {
        List<Token> stringInputList = new Stream(filename, source.toString()).getTokenlist();
        result.addAll(stringInputList);
      }
      if (isFlag(PREPROCESS_FILE_INPUT)) {
        List<Token> fileInputList = Hash_stream.getHashedStream(filename).getTokenlist();
        result.addAll(fileInputList);
      }

      return result;
    }

    private boolean isFlag(int f) {
      return (flag & f) == f;
    }

  }

}
