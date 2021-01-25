package jscan.tokenize;

import static jscan.tokenize.Env.EOF_TOKEN_ENTRY;
import static jscan.tokenize.Env.HC_FEOF;
import static jscan.tokenize.Env.isDec;
import static jscan.tokenize.Env.isLetter;
import static jscan.tokenize.Env.isOpStart;
import static jscan.tokenize.T.TOKEN_EOF;
import static jscan.tokenize.T.TOKEN_ERROR;
import static jscan.tokenize.T.T_DIVIDE;
import static jscan.tokenize.T.T_DIVIDE_EQUAL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ast.IntLiteral;
import ast.ParseIntLiteral;
import jscan.hashed.Hash_ident;
import jscan.main.ScanExc;
import jscan.sourceloc.SourceLocation;

public class Stream {

  private final Token EOL_TOKEN;
  private final Token WSP_TOKEN;

  private final String filename;
  private final List<Token> tokenlist;
  private CBuf buffer;

  private static final Map<String, T> VALID_COMBINATIONS = new HashMap<String, T>();
  private static final Map<String, T> SINGLE_OPERATORS = new HashMap<String, T>();
  private static final Map<String, T> OTHER_ASCII_CHARACTERS = new HashMap<String, T>();

  static {

    // " ... && -= >= ~ + ; ] <: "
    // " <<= &= -> >> % , < ^ :> "
    // " >>= *= /= ^= & - = { <% "
    // " != ++ << |= ( . > | %> "
    // " %= += <= || ) / ? } %: "
    // " ## -- == ! * : [ # %:%: "

    //
    VALID_COMBINATIONS.put("->", T.T_ARROW);
    VALID_COMBINATIONS.put("--", T.T_MINUS_MINUS);
    VALID_COMBINATIONS.put("-=", T.T_MINUS_EQUAL);
    VALID_COMBINATIONS.put("!=", T.T_NE);

    // this is not ANSI, but for easy recognize ellipsis ...
    VALID_COMBINATIONS.put("..", T.T_DOT_DOT);

    VALID_COMBINATIONS.put("*=", T.T_TIMES_EQUAL);
    VALID_COMBINATIONS.put("/=", T.T_DIVIDE_EQUAL);
    VALID_COMBINATIONS.put("&=", T.T_AND_EQUAL);
    VALID_COMBINATIONS.put("&&", T.T_AND_AND);
    VALID_COMBINATIONS.put("##", T.T_SHARP_SHARP);
    VALID_COMBINATIONS.put("%=", T.T_PERCENT_EQUAL);
    VALID_COMBINATIONS.put("^=", T.T_XOR_EQUAL);
    VALID_COMBINATIONS.put("++", T.T_PLUS_PLUS);
    VALID_COMBINATIONS.put("+=", T.T_PLUS_EQUAL);
    VALID_COMBINATIONS.put("<=", T.T_LE);
    VALID_COMBINATIONS.put("<<", T.T_LSHIFT);
    VALID_COMBINATIONS.put("==", T.T_EQ);
    VALID_COMBINATIONS.put(">=", T.T_GE);
    VALID_COMBINATIONS.put(">>", T.T_RSHIFT);
    VALID_COMBINATIONS.put("||", T.T_OR_OR);
    VALID_COMBINATIONS.put("|=", T.T_OR_EQUAL);

    // special handling for this large...
    // %:%:
    // >>=
    // <<=
    // ...

    //
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

    EOL_TOKEN = new Token();
    EOL_TOKEN.setNewLine(true);
    EOL_TOKEN.setValue("\\n");

    WSP_TOKEN = new Token();
    WSP_TOKEN.setLeadingWhitespace(true);
    WSP_TOKEN.setValue(" ");

    tokenize();
    buffer = null; // forget
  }

  private Token specialToken(T _type, String _value) {
    Token token = new Token();
    token.set(_type, _value);

    setPos(token);
    return token;
  }

  private Token identToken(Ident _ident) {
    Token token = new Token();
    token.setIdent(_ident);

    setPos(token);
    return token;
  }

  private void setPos(Token token) {
    final int column = buffer.getColumn() - token.getValue().length();
    token.setLocation(new SourceLocation(filename, buffer.getLine(), column));
  }

  private Token nex2() {

    int c = buffer.nextc();
    int nextchar = buffer.peekc();

    if (c == HC_FEOF) {
      return EOF_TOKEN_ENTRY;
    }

    final boolean isWhiteSpace = c == ' ' || c == '\t' || c == '\f';
    if (isWhiteSpace) {
      return WSP_TOKEN;
    }

    if (c == '\n') {
      return EOL_TOKEN;
    }

    if (c == '/') { /* [//], [/*], [/=], [/] */

      if (nextchar == '/') {
        buffer.nextc(); /* XXX: SHIFT */
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

      else if (nextchar == '*') {
        // throw new ScanExc("c-style comments /**/ are deprecated.");
        buffer.nextc(); /* XXX: SHIFT */
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

      else if (nextchar == '=') {
        buffer.nextc(); /* XXX: SHIFT */
        return specialToken(T_DIVIDE_EQUAL, "/=");
      }

      else {
        return specialToken(T_DIVIDE, "/");
      }

    }

    // string|char
    final boolean isStringStart = (c == '\'' || c == '\"');
    if (isStringStart) {
      return getString(c);
    }

    // identifier 
    if (isLetter(c)) {
      return getOneIdent(c, nextchar);
    }

    // numeric
    if (isDec(c)) {
      return getPpNum(c);
    }

    if (isOpStart(c)) {
      return getOnePunct(c, nextchar);
    }

    String sval = Character.toString((char) c);
    if (c == '$' || c == '@' || c == '`' || c == '\\') {
      T typeoftok = OTHER_ASCII_CHARACTERS.get(sval);
      return specialToken(typeoftok, sval);
    }

    // default unknown
    //
    System.out.println(filename + ":" + buffer.getLine() + " TOKEN_ERROR handle " + sval);
    return specialToken(TOKEN_ERROR, sval);
  }

  public Token getOnePunct(int c, int nextchar) {
    String combOp = Character.toString((char) c) + Character.toString((char) nextchar);

    // I) single
    if (!VALID_COMBINATIONS.containsKey(combOp)) {
      String defaultRet = "";
      defaultRet += (char) c;
      T singleOpType = SINGLE_OPERATORS.get(defaultRet);
      return specialToken(singleOpType, defaultRet);
    }

    // II) combine
    T combType = VALID_COMBINATIONS.get(combOp);
    buffer.nextc(); /* XXX: SHIFT */

    // >>=
    // <<=
    if (combOp.equals(">>") || combOp.equals("<<")) {
      int p = buffer.peekc();
      if (p == '=') {
        buffer.nextc(); /* XXX: SHIFT */
        T shiftAssignType = combOp.equals(">>") ? T.T_RSHIFT_EQUAL : T.T_LSHIFT_EQUAL;
        combOp += (char) p;
        return specialToken(shiftAssignType, combOp);
      }

      return specialToken(combType, combOp);
    }

    // ...
    if (combOp.equals("..")) {
      int p = buffer.nextc();
      if (p == '.') {
        combOp += (char) p;
        return specialToken(T.T_DOT_DOT_DOT, combOp);
      } else {
        return specialToken(T.T_DOT_DOT, combOp);
      }
    }

    // otherwise return two-chars token... ++, --, ->, etc.
    return specialToken(combType, combOp);
  }

  private Token getOneIdent(int c, int nextchar) {

    StringBuilder sb = new StringBuilder();
    sb.append((char) c);

    // tail

    for (;;) {
      int peek1 = buffer.peekc();
      boolean isIdentifierTail = isLetter(peek1) || isDec(peek1);
      if (!isIdentifierTail) {
        break;
      }
      sb.append((char) buffer.nextc());
    }

    return identToken(Hash_ident.getHashedIdent(sb.toString()));

  }

  private Token getString(int c) {
    int endof = (c == '\"' ? '\"' : '\'');
    T typeoftok = (c == '\'') ? T.TOKEN_CHAR : T.TOKEN_STRING;
    StringBuilder strbuf = new StringBuilder();

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

    Token token = new Token();
    setPos(token);
    token.setType(typeoftok);
    token.setValue((char) endof + strbuf.toString() + (char) endof);
    return token;

  }

  public Token getPpNum(int c) {

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
    strbuf.append((char) c);

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
    IntLiteral intLiteral = new ParseIntLiteral(numstr).parse();

    final Token specialToken = specialToken(T.TOKEN_NUMBER, numstr);
    specialToken.setNumconst(intLiteral);

    return specialToken;
  }

  public List<Token> getTokenlist() {
    return tokenlist;
  }

  private void markbegin() {
    Token t = new Token();
    t.setType(T.TOKEN_STREAMBEGIN);
    t.setLocation(new SourceLocation(filename, 0, 0)); // TODO:real pos
    t.setValue("");
    tokenlist.add(t);
  }

  private void markend() {
    Token t = new Token();
    t.setType(T.TOKEN_STREAMEND);
    t.setLocation(new SourceLocation(filename, 0, 0)); // TODO:real pos
    t.setValue("");
    tokenlist.add(t);
  }

  private void tokenize() {
    markbegin();

    LinkedList<Token> line = new LinkedList<Token>();
    boolean nextws = false;

    for (;;) {
      Token t = nex2();

      if (t.ofType(TOKEN_EOF)) {

        tokenlist.addAll(line);
        markend(); // eostream
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
