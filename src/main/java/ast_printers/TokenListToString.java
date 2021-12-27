package ast_printers;

import java.util.List;

import tokenize.Token;

public abstract class TokenListToString {

  public static String print(List<Token> tokens) {
    StringBuilder sb = new StringBuilder();
    for (Token tok : tokens) {
      if (tok.hasLeadingWhitespace()) {
        System.out.printf("%s", " ");
        sb.append(" ");
      }
      sb.append(tok.getValue());
      if (tok.isNewLine()) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }

}
