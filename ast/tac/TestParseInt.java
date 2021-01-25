package ast.tac;

import static ast.types.TypeBase.TP_I16;
import static ast.types.TypeBase.TP_I32;
import static ast.types.TypeBase.TP_I64;
import static ast.types.TypeBase.TP_I8;
import static ast.types.TypeBase.TP_U16;
import static ast.types.TypeBase.TP_U32;
import static ast.types.TypeBase.TP_U64;
import static ast.types.TypeBase.TP_U8;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import ast.parse.AstParseException;
import ast.types.TypeBase;

public class TestParseInt {

  private String buffer;
  private int offset;
  private char current;

  @Before
  public void before() {
    buffer = "";
    offset = 0;
    current = '\0';
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

  private boolean isDec(int c) {
    return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8'
        || c == '9';
  }

  private boolean isHex(int c) {
    return isDec(c) || (c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'E' || c == 'F' || c == 'a' || c == 'b'
        || c == 'c' || c == 'd' || c == 'e' || c == 'f');
  }

  private boolean isOct(int c) {
    return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7';
  }

  private boolean isBin(int c) {
    return c == '0' || c == '1';
  }

  private String getDigitsPart(int r) {
    StringBuilder digits = new StringBuilder();
    for (char c = nextc(); c != '\0'; c = nextc()) {
      if (c == '_') {
        continue;
      }
      if (r == 2 && !(isBin(c) || c == '_')) {
        break;
      }
      if (r == 8 && !(isOct(c) || c == '_')) {
        break;
      }
      if (r == 10 && !(isDec(c) || c == '_')) {
        break;
      }
      if (r == 16 && !(isHex(c) || c == '_')) {
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
      for (char c = nextc(); isDec(c); c = nextc()) {
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

  private IntLiteral parse() {
    char[] lookup = peekc2();
    char c = lookup[0];
    char nextchar = lookup[1];

    if (!isDec(c)) {
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

  @Test
  public void testDec() throws Exception {
    buffer = "0_1_2_3";
    IntLiteral literal = parse();
    assertEquals(TypeBase.TP_I32, literal.getType());
    assertEquals(123, literal.getInteger());
    assertEquals(buffer, literal.getInput());
  }

}
