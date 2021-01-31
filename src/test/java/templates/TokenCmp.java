package templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_main.ParserMain;
import ast_unit.InstantiationUnit;
import tokenize.Stream;
import tokenize.T;
import tokenize.Token;
import utils.UtilSrcToStringLevel;
import utils_fio.FileReadKind;
import utils_fio.FileWrapper;

public class TokenCmp {
  private final String srcFname;
  private final String expFname;

  public TokenCmp(String srcFname, String expFname) {
    this.srcFname = srcFname;
    this.expFname = expFname;
  }

  public void compare() throws IOException {

    List<Token> lexed = new ArrayList<Token>();
    FileWrapper fw = new FileWrapper(expFname);
    fw.assertIsExists();
    fw.assertIsFile();
    for (Token t : new Stream(expFname, fw.readToString(FileReadKind.APPEND_LF)).getTokenlist()) {
      if (t.typeIsSpecialStreamMarks()) {
        continue;
      }
      lexed.add(t);
    }

    InstantiationUnit result = new ParserMain(srcFname).parseInstantiationUnit();
    StringBuilder source = new StringBuilder();
    for (ClassDeclaration clazz : result.getClasses()) {
      source.append(clazz.toString());
    }
    List<Token> preprocessed = new ArrayList<>();
    for (Token t : new Stream(srcFname, source.toString()).getTokenlist()) {
      if (t.typeIsSpecialStreamMarks()) {
        continue;
      }
      preprocessed.add(t);
    }

    int bound = preprocessed.size();

    if (bound != lexed.size()) {
      throw new RuntimeException(
          srcFname + "error: size diff...Preprocessed: " + bound + "; but lexed: " + lexed.size());
    }

    for (int j = 0; j < bound; j++) {
      Token ppTok = preprocessed.get(j);
      Token lxTok = lexed.get(j);

      String ppVal = ppTok.getValue();
      String lxVal = lxTok.getValue();
      if (!ppVal.equals(lxVal)) {
        throw new RuntimeException(
            ppTok.getLocationToString() + " error : value diff: expected[" + lxVal + "] but was : [" + ppVal + "]");
      }

      T pptype = ppTok.getType();
      T lxtype = lxTok.getType();
      if (!pptype.equals(lxtype)) {
        throw new RuntimeException(
            "Type diff: expected[" + lxtype.toString() + "] but was : [" + pptype.toString() + "]");
      }
    }

  }

}
