package ast_main;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast_symtab.IdentMap;
import parse.Parse;
import parse.Tokenlist;
import tokenize.Stream;
import tokenize.T;
import tokenize.Token;
import utils_fio.FileReadKind;
import utils_fio.FileWrapper;

public class ImportsResolver {

  private final String dir;
  private final Set<String> fullyRead;

  public Set<String> getFullyRead() {
    return fullyRead;
  }

  public ImportsResolver(final String thisFname) throws IOException {
    this.dir = System.getProperty("user.dir");
    this.fullyRead = new HashSet<>();

    final String givenName = dir + "/" + thisFname;
    getImports(givenName);
  }

  private void getImports(String filename) throws IOException {

    final FileWrapper fw = new FileWrapper(filename);
    fw.assertIsExists();
    fw.assertIsFile();

    final String fullTextFromThisFile = fw.readToString(FileReadKind.APPEND_LF);
    final List<Token> tokens = new Stream(filename, fullTextFromThisFile).getTokenlist();

    // temporary, do not use ParserMain modules !!!
    final Parse parser = new Parse(new Tokenlist(tokens));

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
      getImports(path);
    }

  }

  private String parseImport(Parse parser) throws IOException {
    parser.checkedMove(IdentMap.import_ident);
    return dir + "/" + getImportName(parser);
  }

  private String getImportName(Parse parser) {

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
    return sb.toString();
  }

}