package utils;

import java.util.List;

import tokenize.Stream;
import tokenize.T;
import tokenize.Token;

public class UtilSrcToStringLevel {

  private static int level = 0;

  public static String tos(String source) {

    level = 0;

    List<Token> tokens = new Stream("test...", source).getTokenlist();
    StringBuilder sb = new StringBuilder();

    int index = 0;

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

      if (index + 1 < tokens.size() - 1) {
        if (tokens.get(index + 1).ofType(T.TOKEN_COMMENT)) {
          sb.append("\n");
        }
      }

      if (tok.ofType(T.T_LEFT_BRACE)) {
        level++;
      }

      index += 1;
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
