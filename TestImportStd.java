package njast;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import jscan.fio.FileReadKind;
import jscan.fio.FileWrapper;
import jscan.fio.Normalizer;
import jscan.tokenize.T;
import jscan.tokenize.Token;
import njast.ast.nodes.ClassDeclaration;
import njast.ast.nodes.unit.CompilationUnit;
import njast.ast.nodes.unit.InstantiationUnit;
import njast.parse.Parse;
import njast.parse.main.ParserMain;
import njast.symtab.IdentMap;
import njast.templates.InstatantiationUnitBuilder;
import njast.utils.UtilSrcToStringLevel;

public class TestImportStd {

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
    final String dir = System.getProperty("user.dir") + "/src/test/java/njast/";
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

  @Test
  public void testImportStdList() throws Exception {

    //@formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append(" /*001*/  import std.list;         \n");
    sb.append(" /*002*/  class test {             \n");
    sb.append(" /*003*/    func main() -> int {   \n");
    sb.append(" /*004*/     var t:string = io.read_file();  var e: list<int>;    \n");
    sb.append(" /*005*/      return 0;            \n");
    sb.append(" /*006*/    }                      \n");
    sb.append(" /*007*/  }                        \n");
    //@formatter:on

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

    StringBuilder source = new StringBuilder();
    source.append(sb);
    source.append("\n");
    source.append(text);
    source.append("\n");
    Parse mainParser = new ParserMain(source).initiateParse();

    CompilationUnit unit = mainParser.parse();
    InstantiationUnit instantiationUnit = new InstatantiationUnitBuilder(unit).getInstantiationUnit();
    for (ClassDeclaration clazz : instantiationUnit.getClasses()) {
      System.out.println(UtilSrcToStringLevel.tos(clazz.toString()));
    }

  }

  private String simpleName(String path) {
    final String res = path.substring(path.lastIndexOf('/') + 1).trim();
    return res;
  }

}
