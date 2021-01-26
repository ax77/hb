package njast;

import java.util.List;

import org.junit.Test;

import tokenize.Stream;
import tokenize.Token;
import utils_fio.FileReadKind;
import utils_fio.FileWrapper;

public class TestTokenizer {
  @Test
  public void testCharZero() throws Exception {
    final String dir = System.getProperty("user.dir");
    FileWrapper fw = new FileWrapper(dir + "/tests/test_string_tokenizer.txt");

    Stream stream = new Stream("...test...", fw.readToString(FileReadKind.AS_IS));
    List<Token> tokens = stream.getTokenlist();

    for (Token tok : tokens) {
      if (tok.typeIsSpecialStreamMarks()) {
        continue;
      }
    }
  }
}
