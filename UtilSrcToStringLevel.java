package njast;

import java.util.List;

import jscan.tokenize.Stream;
import jscan.tokenize.T;
import jscan.tokenize.Token;

public class UtilSrcToStringLevel {

  private static int level = 0;

  public static String tos(String source) {

    level = 0;

    List<Token> tokens = new Stream("test...", source).getTokenlist();
    StringBuilder sb = new StringBuilder();

    for (Token tok : tokens) {
      if (tok.ofType(T.T_RIGHT_BRACE)) {
        --level;
      }
      if (tok.hasLeadingWhitespace()) {
        if (!tok.isAtBol()) {
          sb.append(" ");
        }
      }
      if (tok.isAtBol()) {
        sb.append(pad());
      }
      sb.append(tok.getValue());
      if (tok.isNewLine()) {
        sb.append("\n");
      }
      if (tok.ofType(T.T_LEFT_BRACE)) {
        level++;
      }

    }

    return sb.toString();
  }

  private static String pad() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < level; ++i) {
      sb.append("  ");
    }
    return sb.toString();
  }

}
