package tokenize;

import static tokenize.Env.EOF_TOKEN_ENTRY;
import static tokenize.Env.HC_FEOF;
import static tokenize.Env.isDec;
import static tokenize.Env.isLetter;
import static tokenize.T.TOKEN_EOF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ast_sourceloc.SourceLocation;
import errors.ScanExc;
import hashed.Hash_ident;
import literals.IntLiteral;
import literals.ParseIntLiteral;

public class Stream {

  private final Token EOL_TOKEN;
  private final Token WSP_TOKEN;

  private final String filename;
  private final List<Token> tokenlist;
  private CBuf buffer;

  private static final Map<String, T> VALID_COMBINATIONS_2 = new HashMap<>();
  private static final Map<String, T> VALID_COMBINATIONS_3 = new HashMap<>();
  private static final Map<String, T> SINGLE_OPERATORS = new HashMap<>();
  private static final Map<String, T> OTHER_ASCII_CHARACTERS = new HashMap<>();

  static {

    // " ... && -= >= ~ + ; ] <: "
    // " <<= &= -> >> % , < ^ :> "
    // " >>= *= /= ^= & - = { <% "
    // " != ++ << |= ( . > | %> "
    // " %= += <= || ) / ? } %: "
    // " ## -- == ! * : [ # %:%: "

    // 3
    VALID_COMBINATIONS_3.put(">>=", T.T_RSHIFT_EQUAL);
    VALID_COMBINATIONS_3.put("<<=", T.T_LSHIFT_EQUAL);
    VALID_COMBINATIONS_3.put("...", T.T_DOT_DOT_DOT);

    // 2
    VALID_COMBINATIONS_2.put("->", T.T_ARROW);
    VALID_COMBINATIONS_2.put("--", T.T_MINUS_MINUS);
    VALID_COMBINATIONS_2.put("-=", T.T_MINUS_EQUAL);
    VALID_COMBINATIONS_2.put("!=", T.T_NE);
    VALID_COMBINATIONS_2.put("..", T.T_DOT_DOT);
    VALID_COMBINATIONS_2.put("*=", T.T_TIMES_EQUAL);
    VALID_COMBINATIONS_2.put("/=", T.T_DIVIDE_EQUAL);
    VALID_COMBINATIONS_2.put("&=", T.T_AND_EQUAL);
    VALID_COMBINATIONS_2.put("&&", T.T_AND_AND);
    VALID_COMBINATIONS_2.put("##", T.T_SHARP_SHARP);
    VALID_COMBINATIONS_2.put("%=", T.T_PERCENT_EQUAL);
    VALID_COMBINATIONS_2.put("^=", T.T_XOR_EQUAL);
    VALID_COMBINATIONS_2.put("++", T.T_PLUS_PLUS);
    VALID_COMBINATIONS_2.put("+=", T.T_PLUS_EQUAL);
    VALID_COMBINATIONS_2.put("<=", T.T_LE);
    VALID_COMBINATIONS_2.put("<<", T.T_LSHIFT);
    VALID_COMBINATIONS_2.put("==", T.T_EQ);
    VALID_COMBINATIONS_2.put(">=", T.T_GE);

    /// we'll handle this as a special case in 
    /// expression parsing stage,
    /// because it's much easier to handle this situation: list<list<i32>>
    /// in expression, instead of to rewrite the whole parser logic...
    /// VALID_COMBINATIONS_2.put(">>", T.T_RSHIFT);

    VALID_COMBINATIONS_2.put("||", T.T_OR_OR);
    VALID_COMBINATIONS_2.put("|=", T.T_OR_EQUAL);

    // 1
    SINGLE_OPERATORS.put(",", T.T_COMMA);
    SINGLE_OPERATORS.put("-", T.T_MINUS);
    SINGLE_OPERATORS.put(";", T.T_SEMI_COLON);
    SINGLE_OPERATORS.put(":", T.T_COLON);
    SINGLE_OPERATORS.put("!", T.T_EXCLAMATION);
    SINGLE_OPERATORS.put("?", T.T_QUESTION);
    SINGLE_OPERATORS.put(".", T.T_DOT);
    SINGLE_OPERATORS.put("(", T.T_LEFT_PAREN);
    SINGLE_OPERATORS.put(")", T.T_RIGHT_PAREN);
    SINGLE_OPERATORS.put("[", T.T_LEFT_BRACKET);
    SINGLE_OPERATORS.put("]", T.T_RIGHT_BRACKET);
    SINGLE_OPERATORS.put("{", T.T_LEFT_BRACE);
    SINGLE_OPERATORS.put("}", T.T_RIGHT_BRACE);
    SINGLE_OPERATORS.put("*", T.T_TIMES);
    SINGLE_OPERATORS.put("/", T.T_DIVIDE);
    SINGLE_OPERATORS.put("&", T.T_AND);
    SINGLE_OPERATORS.put("#", T.T_SHARP);
    SINGLE_OPERATORS.put("%", T.T_PERCENT);
    SINGLE_OPERATORS.put("^", T.T_XOR);
    SINGLE_OPERATORS.put("+", T.T_PLUS);
    SINGLE_OPERATORS.put("<", T.T_LT);
    SINGLE_OPERATORS.put("=", T.T_ASSIGN);
    SINGLE_OPERATORS.put(">", T.T_GT);
    SINGLE_OPERATORS.put("|", T.T_OR);
    SINGLE_OPERATORS.put("~", T.T_TILDE);

    // 
    OTHER_ASCII_CHARACTERS.put("$", T.T_DOLLAR_SIGN);
    OTHER_ASCII_CHARACTERS.put("@", T.T_AT_SIGN);
    OTHER_ASCII_CHARACTERS.put("`", T.T_GRAVE_ACCENT);
    OTHER_ASCII_CHARACTERS.put("\\", T.T_BACKSLASH);
  }

  public Stream(String _fname, String _txt) {
    filename = _fname;
    tokenlist = new ArrayList<Token>();
    buffer = new CBuf(_txt);

    EOL_TOKEN = new Token("\\n", T.TOKEN_ERROR, builtinZeroLocation(T.TOKEN_ERROR));
    EOL_TOKEN.setNewLine(true);

    WSP_TOKEN = new Token(" ", T.TOKEN_ERROR, builtinZeroLocation(T.TOKEN_ERROR));
    WSP_TOKEN.setLeadingWhitespace(true);

    tokenize();
    buffer = null; // forget
  }

  private SourceLocation builtinZeroLocation(T type) {
    return new SourceLocation(type.toString(), -1, -1);
  }

  private Token specialToken(T type, String value) {
    return new Token(value, type, curLoc());
  }

  private Token identToken(Ident ident) {
    return new Token(ident, curLoc());
  }

  private SourceLocation curLoc() {
    return new SourceLocation(filename, buffer.getLine(), -1);
  }

  private void move() {
    buffer.nextc();
  }

  private Token nex2() {

    final char[] threechars = buffer.peekc3();
    final char c1 = threechars[0];
    final char c2 = threechars[1];
    final char c3 = threechars[2];

    if (c1 == HC_FEOF) {
      return EOF_TOKEN_ENTRY;
    }

    final boolean isWhiteSpace = c1 == ' ' || c1 == '\t' || c1 == '\f';
    if (isWhiteSpace) {
      move();
      return WSP_TOKEN;
    }

    if (c1 == '\n') {
      move();
      return EOL_TOKEN;
    }

    if (c1 == '/') { /* [//], [/*], [/=], [/] */

      if (c2 == '/') {
        move();
        move();
        for (;;) {
          int tmpch = buffer.nextc();
          if (tmpch == '\n') {
            return EOL_TOKEN;
          }
          if (tmpch == HC_FEOF) {
            return EOF_TOKEN_ENTRY;
          }
        }
      }

      else if (c2 == '*') {
        move();
        move();
        int prevc = '\0';
        for (;;) {
          int tmpch = buffer.nextc();
          if (tmpch == HC_FEOF) {
            throw new ScanExc(Integer.toString(buffer.getLine()));
          }
          if (tmpch == '/' && prevc == '*') {
            return WSP_TOKEN;
          }
          prevc = tmpch;
        }
      }

    }

    // string|char
    final boolean isStringStart = (c1 == '\'' || c1 == '\"');
    if (isStringStart) {
      return getString();
    }

    // identifier 
    if (isLetter(c1)) {
      return getOneIdent();
    }

    // numeric
    if (isDec(c1)) {
      return getPpNum();
    }

    // operators
    if (Env.isOpStart(c1)) {
      return getOperator(c1, c2, c3);
    }

    // specials, reserved
    final String others = combineOp(c1);
    if (OTHER_ASCII_CHARACTERS.containsKey(others)) {
      move();
      return specialToken(OTHER_ASCII_CHARACTERS.get(others), others);
    }

    throw new ScanExc("unknown source: " + combineOp(c1, c2, c3));
  }

  /// this function is not-optimized, of course
  /// but it works as primitive and simple as possible
  /// 
  private Token getOperator(char c1, char c2, char c3) {

    // from top to bottom

    final String three = combineOp(c1, c2, c3);
    if (VALID_COMBINATIONS_3.containsKey(three)) {
      move();
      move();
      move();
      return specialToken(VALID_COMBINATIONS_3.get(three), three);
    }

    final String two = combineOp(c1, c2);
    if (VALID_COMBINATIONS_2.containsKey(two)) {
      move();
      move();
      return specialToken(VALID_COMBINATIONS_2.get(two), two);
    }

    final String one = combineOp(c1);
    if (SINGLE_OPERATORS.containsKey(one)) {
      move();
      return specialToken(SINGLE_OPERATORS.get(one), one);
    }

    throw new ScanExc("unknown operator: " + three);
  }

  private String combineOp(char... ops) {
    StringBuilder sb = new StringBuilder();
    for (char op : ops) {
      if (op == '\0') {
        continue;
      }
      sb.append(op);
    }
    return sb.toString();
  }

  private Token getOneIdent() {

    final StringBuilder sb = new StringBuilder();

    for (;;) {
      char peek1 = buffer.peekc();
      boolean isIdentifierTail = isLetter(peek1) || isDec(peek1);
      if (!isIdentifierTail) {
        break;
      }
      sb.append((char) buffer.nextc());
    }

    return identToken(Hash_ident.getHashedIdent(sb.toString()));

  }

  private Token getString() {

    char c = buffer.nextc();

    final char endof = (c == '\"' ? '\"' : '\'');
    final StringBuilder strbuf = new StringBuilder();

    for (;;) {
      int next1 = buffer.nextc();
      if (next1 == Env.HC_FEOF) {
        throw new ScanExc(Integer.toString(buffer.getLine()));
      }
      if (next1 == '\n') {
        throw new ScanExc(Integer.toString(buffer.getLine()));
      }
      if (next1 == endof) {
        break;
      }
      if (next1 != '\\') {
        strbuf.append((char) next1);
        continue;
      }
      int next2 = buffer.nextc();
      strbuf.append("\\");
      strbuf.append((char) next2);
    }

    // TODO:
    final String repr = endof + strbuf.toString() + endof;
    final int escaped[] = new CEscaper(new CBuf(strbuf.toString())).escape();

    if (endof == '\"') {
      return new Token(escaped, repr, curLoc());
    }

    // chars

    if (escaped.length == 0) {
      throw new ScanExc("" + " error : empty char constant");
    }
    if (escaped.length > 2) {
    }

    return new Token((char) escaped[0], curLoc());

  }

  public Token getPpNum() {

    /*
     * pp-number:
     *   digit
     *   . digit
     *   pp-number digit
     *   pp-number identifier-nondigit
     *   pp-number e sign
     *   pp-number E sign
     *   pp-number .
     */

    StringBuilder strbuf = new StringBuilder();

    for (;;) {
      int peekc = buffer.peekc();
      if (isDec(peekc)) {
        strbuf.append((char) buffer.nextc());
        continue;
      } else if (peekc == 'e' || peekc == 'E' || peekc == 'p' || peekc == 'P') {
        strbuf.append((char) buffer.nextc());

        peekc = buffer.peekc();
        if (peekc == '-' || peekc == '+') {
          strbuf.append((char) buffer.nextc());
        }
        continue;
      } else if (peekc == '.' || isLetter(peekc)) {
        strbuf.append((char) buffer.nextc());
        continue;
      }

      break;
    }

    final String numstr = strbuf.toString();
    final IntLiteral intLiteral = new ParseIntLiteral(numstr, EOL_TOKEN).parse(); // TODO: location

    return new Token(intLiteral, curLoc());
  }

  public List<Token> getTokenlist() {
    return tokenlist;
  }

  private void tokenize() {

    LinkedList<Token> line = new LinkedList<Token>();
    boolean nextws = false;

    for (;;) {
      Token t = nex2();

      if (t.ofType(TOKEN_EOF)) {

        tokenlist.addAll(line);
        tokenlist.add(t); // eof

        break;
      }

      if (nextws) {
        t.setLeadingWhitespace(true);
        nextws = false;
      }

      if (t == EOL_TOKEN) {
        if (line.isEmpty()) {
          continue;
        }
        line.getLast().setNewLine(true);
        line.getFirst().setAtBol(true);
        line.getFirst().setLeadingWhitespace(true);

        tokenlist.addAll(line);
        line.clear();
        continue;
      }

      if (t == WSP_TOKEN) {
        nextws = true;
        continue;
      }

      line.add(t);
    }
  }

}
