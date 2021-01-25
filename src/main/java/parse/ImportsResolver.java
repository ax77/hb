package parse;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import ast_symtab.IdentMap;
import tokenize.T;
import tokenize.Token;
import utils_fio.FileReadKind;
import utils_fio.FileWrapper;
import utils_oth.Normalizer;

public class ImportsResolver {

  private final StringBuilder source = new StringBuilder();

  public StringBuilder getSource() {
    return source;
  }

  public ImportsResolver(StringBuilder sb) throws IOException {

    final String thisFname = "/test";
    Set<String> fullyRead = new HashSet<>();
    getImports(thisFname, sb, fullyRead);

    StringBuilder text = new StringBuilder();
    for (String path : fullyRead) {
      if (path.equals(thisFname)) {
        continue;
      }
      FileWrapper fw = new FileWrapper(path);
      text.append(fw.readToString(FileReadKind.APPEND_LF));
    }

    source.append(sb);
    source.append("\n");
    source.append(text);
    source.append("\n");
  }

  private void getImports(String filename, StringBuilder sb, Set<String> fullyRead) throws IOException {

    Parse parser = new ParserMain(sb).initiateParse();

    Set<String> imports = new HashSet<>();
    while (!parser.isEof()) {
      if (parser.is(IdentMap.import_ident)) {
        imports.add(parseImport(parser));
      } else {
        parser.move();
      }
    }
    fullyRead.add(filename);

    for (String path : imports) {
      if (fullyRead.contains(path)) {
        continue;
      }
      FileWrapper fw = new FileWrapper(path);
      getImports(path, new StringBuilder(fw.readToString(FileReadKind.APPEND_LF)), fullyRead);
    }

  }

  private String parseImport(Parse parser) throws IOException {

    Token tok = parser.checkedMove(IdentMap.import_ident);
    StringBuilder sb = getImportName(parser);

    // TODO: normal directory for imports.
    // now: just like this, because it is easy to manage git repository
    // with these std files, and it is easy and precise to access the files
    // from test folder instead of the root of the app.
    final String dir = System.getProperty("user.dir");
    final String path = dir + "/" + sb.toString();

    return Normalizer.normalize(path);
  }

  private StringBuilder getImportName(Parse parser) {

    StringBuilder sb = new StringBuilder();

    while (!parser.isEof()) {

      boolean isOk = parser.is(T.TOKEN_IDENT) || parser.is(T.T_DOT) || parser.is(T.T_SEMI_COLON);
      if (!isOk) {
        parser.perror("expect import path like: [import package.file;]");
      }
      if (parser.is(T.T_SEMI_COLON)) {
        break;
      }
      if (parser.isEof()) {
        parser.perror("unexpected EOF");
      }

      Token importname = parser.moveget();
      if (importname.ofType(T.T_DOT)) {
        sb.append("/");
      } else {
        sb.append(importname.getValue());
      }

    }

    parser.semicolon();
    return sb;
  }

}
