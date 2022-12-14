package literals;

import static tokenize.Env.HC_FEOF;
import static tokenize.Env.isBin;
import static tokenize.Env.isDec;
import static tokenize.Env.isHex;
import static tokenize.Env.isLetter;
import static tokenize.Env.isOct;

import java.util.LinkedList;

import ast_types.Type;
import ast_types.TypeBindings;
import tokenize.CBuf;
import utils_oth.NullChecker;

public abstract class ParseIntLiteral {

  private static void cutForBase(LinkedList<Character> buffer, StringBuilder out, int base) {

    final boolean baseIsCorrect = base == 2 || base == 8 || base == 10 || base == 16;
    if (!baseIsCorrect) {
      throw new RuntimeException("it is not a correct base: " + base);
    }

    for (; !buffer.isEmpty();) {
      final Character peek = buffer.peekFirst();
      final boolean needBreak = (base == 2 && !isBin(peek)) || (base == 8 && !isOct(peek))
          || (base == 10 && !isDec(peek)) || (base == 16 && !isHex(peek));
      if (needBreak) {
        break;
      }

      char c = buffer.removeFirst();
      out.append(c);
    }
  }

  private static char cutMntExp(LinkedList<Character> buffer, StringBuilder mnt, StringBuilder exp, int mntBase) {

    final boolean baseIsCorrect = mntBase == 10 || mntBase == 16;
    if (!baseIsCorrect) {
      throw new RuntimeException("it is not a correct base for a mantissa: " + mntBase);
    }

    char exp_sign = '+';
    if (!buffer.isEmpty() && buffer.peekFirst() == '.') {
      buffer.removeFirst();
      cutForBase(buffer, mnt, mntBase);
    }

    if (!buffer.isEmpty()) {

      final boolean isHexExp = buffer.peekFirst() == 'p' || buffer.peekFirst() == 'P';
      final boolean isDecExp = buffer.peekFirst() == 'e' || buffer.peekFirst() == 'E';

      if (isHexExp && mntBase != 16) {
        throw new RuntimeException("a hex exponent part in a floating constant.");
      }

      if (isHexExp || isDecExp) {
        buffer.removeFirst();
        if (buffer.peekFirst() == '-' || buffer.peekFirst() == '+') {
          exp_sign = buffer.removeFirst();
        }
        cutForBase(buffer, exp, 10);
      }
    }
    return exp_sign;
  }

  public static IntLiteral parse(String input) {

    if (input == null || input.trim().length() == 0) {
      throw new RuntimeException("An empty input.");
    }

    // slight underscores support
    // we do not check whether the underscore is between two digits or it isn't.
    // we may just ignore them all.
    //
    input = input.replaceAll("_", "");

    final LinkedList<Character> buffer = new LinkedList<Character>();
    for (char c : input.toCharArray()) {
      // allow all letters, digits, dot, plus, minus,
      final boolean charIsOk = isLetter(c) || isDec(c) || c == '-' || c == '+' || c == '.';
      if (!charIsOk) {
        throw new RuntimeException("not a number: " + input);
      }

      buffer.add(c);
    }

    char main_sign = '+';
    if (buffer.peekFirst() == '-' || buffer.peekFirst() == '+') {
      main_sign = buffer.removeFirst();
    }

    StringBuilder dec = new StringBuilder();
    StringBuilder mnt = new StringBuilder();
    StringBuilder exp = new StringBuilder();
    StringBuilder suf = new StringBuilder();
    char exp_sign = '+';

    boolean isBin = false;
    boolean isOct = false;
    boolean isHex = false;

    if (buffer.size() > 2) {
      char c1 = buffer.get(0);
      char c2 = buffer.get(1);
      if (c1 == '0' && (c2 == 'b' || c2 == 'B')) {
        isBin = true;
      }
      if (c1 == '0' && (c2 == 'o' || c2 == 'O')) {
        isOct = true;
      }
      if (c1 == '0' && (c2 == 'x' || c2 == 'X')) {
        isHex = true;
      }
    }

    if (isBin || isOct || isHex) {
      buffer.removeFirst();
      buffer.removeFirst();

      // we'he checked that the buffer.size() > 2,
      // and now we know, that there's something here.

      if (isBin) {
        cutForBase(buffer, dec, 2);
      }

      if (isOct) {
        cutForBase(buffer, dec, 8);
      }

      if (isHex) {
        cutForBase(buffer, dec, 16);
        exp_sign = cutMntExp(buffer, mnt, exp, 16);
      }

    }

    else {

      if (buffer.peekFirst() == '.') {
        exp_sign = cutMntExp(buffer, mnt, exp, 10);
      }

      else {

        // parse decimal|floating|floating_exponent

        if (!isDec(buffer.get(0))) {
          throw new RuntimeException("not a number: " + input);
        }

        cutForBase(buffer, dec, 10);
        exp_sign = cutMntExp(buffer, mnt, exp, 10);
      }

    }

    while (!buffer.isEmpty()) {
      suf.append(buffer.removeFirst());
    }

    ///////
    // TODO:TODO:TODO: evaluation
    ///////

    boolean isFloating = false;
    if (mnt.length() != 0) {
      isFloating = true;
    }
    if (exp.length() != 0) {
      isFloating = true;
    }

    IntLiteral ret = new IntLiteral(input, main_sign, dec.toString(), mnt.toString(), exp.toString(), suf.toString(),
        exp_sign);

    if (isBin || isOct) {
      final int base = isBin ? 2 : 8;
      long value = evalLong(base, dec.toString());
      ret.setLong(value);
    }

    else if (isHex) {
      if (isFloating) {
        double value = evalhexfloat(dec.toString(), mnt.toString(), exp.toString(), Character.toString(exp_sign));
        ret.setDouble(value);
      } else {
        long value = evalLong(16, dec.toString());
        ret.setLong(value);
      }
    }

    else {
      // plain decimal, or floating
      if (isFloating) {
        double value = evaldecfloat(dec.toString(), mnt.toString(), exp.toString(), Character.toString(exp_sign));
        ret.setDouble(value);
      } else {
        long value = evalLong(10, dec.toString());
        ret.setLong(value);
      }
    }

    Type type = null;
    if (suf.length() != 0) {
      type = TypeBindings.getTypeBySuffix(suf.toString());
    } else {
      if (isFloating) {
        type = TypeBindings.make_float(); // TODO: of course :)
      } else {
        type = TypeBindings.make_int(); // TODO: of course :)
      }
    }

    NullChecker.check(type);
    ret.setType(type);

    return ret;
  }

  private static LinkedList<Character> strToList(String what) {
    LinkedList<Character> ret = new LinkedList<>();
    for (char c : what.toCharArray()) {
      ret.add(c);
    }
    return ret;
  }

  private static long evalLong(int base, String dec) {

    long retval = 0;

    LinkedList<Character> buffer = strToList(dec);
    while (!buffer.isEmpty()) {
      char c = buffer.removeFirst();
      if (!charCorrectForBase(c, base)) {
        throw new RuntimeException("the character is not suit for base: " + c);
      }
      retval = retval * base + cv(base, c);
    }

    return retval;
  }

  private static boolean charCorrectForBase(int C, int base) {
    if (base == 16 && isHex(C)) {
      return true;
    }
    if (base == 10 && isDec(C)) {
      return true;
    }
    if (base == 8 && isOct(C)) {
      return true;
    }
    if (base == 2 && isBin(C)) {
      return true;
    }
    return false;
  }

  private static double evaldecfloat(String dec, String mnt, String exp, String sig) {

    CBuf decbuf = new CBuf(dec);

    double realval = 0.0;

    for (int c = decbuf.nextc(); c != HC_FEOF; c = decbuf.nextc()) {
      realval = realval * 10.0 + (double) cv(10, c);
    }

    if (!mnt.isEmpty()) {
      CBuf mntbuf = new CBuf(mnt);

      double m = 0.1;
      for (int c = mntbuf.nextc(); c != HC_FEOF; c = mntbuf.nextc()) {
        realval = realval + (double) cv(10, c) * m;
        m *= 0.1;
      }
    }

    boolean div = false;
    if (!sig.isEmpty() && sig.equals("-")) {
      div = true;
    }

    if (!exp.isEmpty()) {

      CBuf expbuf = new CBuf(exp);

      int pow = 0;
      for (int c = expbuf.nextc(); c != HC_FEOF; c = expbuf.nextc()) {
        pow = pow * 10 + (int) cv(10, c);
      }
      double m = 1.0;
      for (int i = 0; i < pow; i++) {
        m *= 10.0;
      }
      if (div) {
        realval /= m;
      } else {
        realval *= m;
      }

    }

    return realval;

  }

  private static double evalhexfloat(String dec, String mnt, String exp, String sig) {

    CBuf decbuf = new CBuf(dec);

    double realval = 0.0;
    for (int c = decbuf.nextc(); c != HC_FEOF; c = decbuf.nextc()) {
      double cv = (double) cv(16, c);
      realval = realval * 16.0 + cv;
    }

    if (!mnt.isEmpty()) {
      CBuf mntbuf = new CBuf(mnt);

      double m = 0.0625;
      for (int c = mntbuf.nextc(); c != HC_FEOF; c = mntbuf.nextc()) {
        double cv = (double) cv(16, c);
        realval = realval + cv * m;
        m *= 0.0625;
      }
    }

    boolean div = false;
    if (!sig.isEmpty() && sig.equals("-")) {
      div = true;
    }

    if (!exp.isEmpty()) {

      CBuf expbuf = new CBuf(exp);

      //
      int pow = 0;
      for (int c = expbuf.nextc(); c != HC_FEOF; c = expbuf.nextc()) {
        pow = pow * 10 + (int) cv(10, c);
      }
      //
      double m = 1.0;
      for (int i = 0; i < pow; i++) {
        m *= 2.0;
      }
      //
      if (div) {
        realval /= m;
      } else {
        realval *= m;
      }

    }

    return realval;

  }

//@formatter:off
  private static  int cv(int base, int c) {
    
    boolean baseInRange = (base == 2) || (base == 8) || (base == 10) || (base == 16);
    if (!baseInRange) {
      throw new RuntimeException("error eval base = " + base + " for char=" + (char) c);
    }
    
    if (base == 2) {
      switch (c) {
      case '0': return 0;
      case '1': return 1;
      default:
        throw new RuntimeException("error eval bin base = " + base + " for char=" + (char) c);
      }
    }
    
    if (base == 8) {
      switch (c) {
      case '0': return 0;
      case '1': return 1;
      case '2': return 2;
      case '3': return 3;
      case '4': return 4;
      case '5': return 5;
      case '6': return 6;
      case '7': return 7;
      default:
        throw new RuntimeException("error eval oct base = " + base + " for char=" + (char) c);
      }
    }
    
    if (base == 10) {
      switch (c) {
      case '0': return 0;
      case '1': return 1;
      case '2': return 2;
      case '3': return 3;
      case '4': return 4;
      case '5': return 5;
      case '6': return 6;
      case '7': return 7;
      case '8': return 8;
      case '9': return 9;
      default:
        throw new RuntimeException("error eval int base = " + base + " for char=" + (char) c);
      }
    }
    
    if (base == 16) {
      switch (c) {
      case '0': return 0;
      case '1': return 1;
      case '2': return 2;
      case '3': return 3;
      case '4': return 4;
      case '5': return 5;
      case '6': return 6;
      case '7': return 7;
      case '8': return 8;
      case '9': return 9;
      case 'a': case 'A': return 10;
      case 'b': case 'B': return 11;
      case 'c': case 'C': return 12;
      case 'd': case 'D': return 13;
      case 'e': case 'E': return 14;
      case 'f': case 'F': return 15;
      default:
        throw new RuntimeException("error eval hex base = " + base + " for char=" + (char) c);
      }
    }
    
    throw new RuntimeException("error eval base = " + base + " for char=" + (char) c);
  }
  //@formatter:on

}
