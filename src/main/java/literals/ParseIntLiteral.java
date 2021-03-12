package literals;

import ast_types.Type;
import ast_types.TypeBindings;
import errors.AstParseException;
import tokenize.Env;
import tokenize.Token;

public class ParseIntLiteral {
  private final String buffer;
  private final Token beginPos;
  private int offset;
  private char current;

  public ParseIntLiteral(String buffer, Token beginPos) {
    this.buffer = buffer;
    this.beginPos = beginPos;
  }

  public IntLiteral parse() {
    char[] lookup = peekc2();
    char c = lookup[0];
    char nextchar = lookup[1];

    if (!Env.isDec(c)) {
      throw new AstParseException("not a digit: [" + buffer + "]");
    }

    // just zero
    if (c == '0' && nextchar == '\0') {
      return new IntLiteral("0", TypeBindings.make_int(), 0); //TODO:
    }

    if (c == '0' && nextchar == 'b') {
      return parseByRadix(2);
    }

    if (c == '0' && nextchar == 'o') {
      return parseByRadix(8);
    }

    if (c == '0' && nextchar == 'x') {
      return parseByRadix(16);
    }

    return parseByRadix(10);
  }

  private char nextc() {
    if (offset >= buffer.length()) {
      return '\0';
    }
    final char c = buffer.charAt(offset);
    offset += 1;
    current = c;
    return c;
  }

  private char[] peekc2() {
    char[] res = new char[2];
    int soffset = offset;
    char scurrent = current;
    res[0] = nextc();
    res[1] = nextc();
    offset = soffset;
    current = scurrent;
    return res;
  }

  private String getDigitsPart(int r) {
    StringBuilder digits = new StringBuilder();
    for (char c = nextc(); c != '\0'; c = nextc()) {
      if (c == '_') {
        continue;
      }
      if (r == 2 && !(Env.isBin(c) || c == '_')) {
        break;
      }
      if (r == 8 && !(Env.isOct(c) || c == '_')) {
        break;
      }
      if (r == 10 && !(Env.isDec(c) || c == '_')) {
        break;
      }
      if (r == 16 && !(Env.isHex(c) || c == '_')) {
        break;
      }
      digits.append(c);
    }
    return digits.toString();
  }

  private String getSuffixPart() {
    // u8 | u16 | u32 | u64
    // i8 | i16 | i32 | i64
    StringBuilder suffix = new StringBuilder();
    if (current == 'i' || current == 'u') {
      suffix.append(current);
      for (char c = nextc(); Env.isDec(c); c = nextc()) {
        suffix.append(c);
      }
    }
    return suffix.toString();
  }

  private Type bindSuffixOrI32ByDefault(String suffix) {
    if (suffix.isEmpty()) {
      return TypeBindings.make_int(); // TODO:
    }
    return TypeBindings.getTypeBySuffix(suffix);
  }

  private IntLiteral parseByRadix(int r) {
    if (r == 2 || r == 8 || r == 16) {
      char c1 = nextc();
      char c2 = nextc();
      boolean isOk = c1 == '0' && (c2 == 'b' || c2 == 'o' || c2 == 'x');
      if (!isOk) {
        throw new AstParseException("unknown prefix: " + c1 + c2);
      }
    }

    String digits = getDigitsPart(r);
    String suffix = getSuffixPart();

    long integer = Long.parseLong(digits, r);
    Type result = bindSuffixOrI32ByDefault(suffix);

    return new IntLiteral(new String(buffer), result, integer);
  }

}
