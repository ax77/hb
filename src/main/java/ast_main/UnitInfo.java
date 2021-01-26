package ast_main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import tokenize.Env;
import tokenize.Stream;
import tokenize.Token;
import utils_fio.FileReadKind;
import utils_fio.FileWrapper;

public class UnitInfo {

  private final Map<String, String> classLocations;
  private final List<Token> tokenlist;

  public UnitInfo(String unitName) throws IOException {
    this.classLocations = new HashMap<>();
    this.tokenlist = new ArrayList<>();
    buildLocations(unitName);
    buildTokenlist();
  }

  // merge all classes into one list of tokens.
  // it works actually like 100% #include, and not like 'import'
  // we need to know each class, because we have template expansion
  // at compilation stage, and if some templates are locate in 
  // different files and depends on each other cyclic - and it is an
  // unsolved names resolution. 
  // I'll fix this later, by now it means nothing.
  // all optimizations at this stage are disallowed.
  // 

  private void buildTokenlist() throws IOException {
    for (Entry<String, String> e : classLocations.entrySet()) {
      FileWrapper fw = new FileWrapper(e.getValue());
      Stream stream = new Stream(e.getValue(), fw.readToString(FileReadKind.AS_IS));
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

  // key :: class-name -> in its pure form like list/string/node
  // val :: full path to this class
  //
  // we do not allow multiple classes in the same file
  // each file-name must be equal to the class which it defines
  // 
  private void buildLocations(final String unitName) throws IOException {
    final ImportsResolver importsResolver = new ImportsResolver(unitName);
    for (String s : importsResolver.getFullyRead()) {
      FileWrapper fw = new FileWrapper(s);
      fw.assertIsExists();
      fw.assertIsFile();
      classLocations.put(fw.getBasename(), fw.getFullname());
    }
  }

  public Map<String, String> getClassLocations() {
    return classLocations;
  }

  public List<Token> getTokenlist() {
    return tokenlist;
  }

}