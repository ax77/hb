package ast_main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ast_class.ClassDeclaration;
import tokenize.Env;
import tokenize.Stream;
import tokenize.Token;
import utils_fio.FileReadKind;
import utils_fio.FileWrapper;

public class UnitInfo {

  private final List<String> classLocations;
  private final List<Token> tokenlist;
  private final List<ClassDeclaration> typenames;

  public UnitInfo(String unitName) throws IOException {
    this.classLocations = new ArrayList<>();
    this.tokenlist = new ArrayList<>();

    buildLocations(unitName);
    buildTokenlist();
    this.typenames = new TypenamesFinder(tokenlist).getTypenames();
  }

  private void buildTokenlist() throws IOException {
    for (String absolutePath : classLocations) {
      final FileWrapper fw = new FileWrapper(absolutePath);
      final Stream stream = new Stream(absolutePath, fw.readToString(FileReadKind.AS_IS));
      final List<Token> tokens = stream.getTokenlist();
      for (Token tok : tokens) {
        // ignoring EOF, and stream-markers
        if (tok.typeIsSpecialStreamMarks()) {
          continue;
        }
        tokenlist.add(tok);
      }
    }
    // !!! EOF here
    tokenlist.add(Env.EOF_TOKEN_ENTRY);
  }

  // key   :: type-name in its pure form like 'list', 'string', 'node', etc...
  // value :: full path to this class
  //
  private void buildLocations(final String unitName) throws IOException {
    final UnitImportsSet importsResolver = new UnitImportsSet(unitName);
    final Set<String> fullyRead = importsResolver.getFullyRead();
    for (String s : fullyRead) {
      FileWrapper fw = new FileWrapper(s);
      fw.assertIsExists();
      fw.assertIsFile();
      classLocations.add(fw.getFullname());
    }
  }

  public List<Token> getTokenlist() {
    return tokenlist;
  }

  public List<ClassDeclaration> getTypenames() {
    return typenames;
  }

}