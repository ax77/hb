package ast;

import static ast.types.TypeBase.TP_I16;
import static ast.types.TypeBase.TP_I32;
import static ast.types.TypeBase.TP_I64;
import static ast.types.TypeBase.TP_I8;
import static ast.types.TypeBase.TP_U16;
import static ast.types.TypeBase.TP_U32;
import static ast.types.TypeBase.TP_U64;
import static ast.types.TypeBase.TP_U8;

import java.util.HashMap;

import ast.parse.AstParseException;
import ast.types.TypeBase;
import jscan.tokenize.Env;

public class ParseIntLiteral {
  private final String buffer;
  private int offset;
  private char current;

  public ParseIntLiteral(String buffer) {
    this.buffer = buffer;
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
      return new IntLiteral("0", TypeBase.TP_I32, 0);
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

  // INTEGER_LITERAL :
  //    ( DEC_LITERAL | BIN_LITERAL | OCT_LITERAL | HEX_LITERAL ) INTEGER_SUFFIX?
  // 
  // DEC_LITERAL :
  //    DEC_DIGIT (DEC_DIGIT|_)*
  // 
  // BIN_LITERAL :
  //    0b (BIN_DIGIT|_)* BIN_DIGIT (BIN_DIGIT|_)*
  // 
  // OCT_LITERAL :
  //    0o (OCT_DIGIT|_)* OCT_DIGIT (OCT_DIGIT|_)*
  // 
  // HEX_LITERAL :
  //    0x (HEX_DIGIT|_)* HEX_DIGIT (HEX_DIGIT|_)*
  // 
  // BIN_DIGIT : [0-1]
  // 
  // OCT_DIGIT : [0-7]
  // 
  // DEC_DIGIT : [0-9]
  // 
  // HEX_DIGIT : [0-9 a-f A-F]
  // 
  // INTEGER_SUFFIX :
  //      u8 | u16 | u32 | u64 | u128 | usize
  //    | i8 | i16 | i32 | i64 | i128 | isize

  // CHAR_BIT   = 8
  // MB_LEN_MAX = 16
  //  
  // CHAR_MIN   = -128
  // CHAR_MAX   = +127
  // SCHAR_MIN  = -128
  // SCHAR_MAX  = +127
  // UCHAR_MAX  = 255
  //  
  // SHRT_MIN   = -32768
  // SHRT_MAX   = +32767
  // USHRT_MAX  = 65535
  //  
  // INT_MIN    = -2147483648
  // INT_MAX    = +2147483647
  // UINT_MAX   = 4294967295
  //  
  // LONG_MIN   = -9223372036854775808
  // LONG_MAX   = +9223372036854775807
  // ULONG_MAX  = 18446744073709551615

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

  private static HashMap<String, TypeBase> bind = new HashMap<>();
  static {
    bind.put("u8", TP_U8);
    bind.put("u16", TP_U16);
    bind.put("u32", TP_U32);
    bind.put("u64", TP_U64);
    bind.put("i8", TP_I8);
    bind.put("i16", TP_I16);
    bind.put("i32", TP_I32);
    bind.put("i64", TP_I64);
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

  private TypeBase bindSuffixOrI32ByDefault(String suffix) {
    if (suffix.isEmpty()) {
      return TP_I32;
    }

    TypeBase result = bind.get(suffix.toString());
    if (result == null) {
      throw new AstParseException("unknown suffix: " + suffix.toString());
    }
    return result;
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
    TypeBase result = bindSuffixOrI32ByDefault(suffix);

    return new IntLiteral(new String(buffer), result, integer);
  }

}
