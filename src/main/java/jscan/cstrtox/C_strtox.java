package jscan.cstrtox;

import static jscan.main.Env.HC_FEOF;
import static jscan.main.Env.isBin;
import static jscan.main.Env.isDec;
import static jscan.main.Env.isHex;
import static jscan.main.Env.isOct;

import java.util.Map;
import java.util.TreeMap;

import jscan.cspec.CBuf;

public class C_strtox {

  private final String input;
  private final int size;
  private NumFlagEval flag;

  private NumType numtype;
  private final Map<String, NumType> INT_SUFFIX_TABLE = new TreeMap<String, NumType>();

  private String dec;
  private String mnt;
  private String exp;
  private String suf;
  private String sig; // [+][-]

  private long clong;
  private float cfloat;
  private double cdouble;

  private boolean hexFloatExp;
  private boolean decFloatExp;

  public C_strtox(String _input) {
    this.input = _input;
    this.size = _input.length();
    this.flag = NumFlagEval.E_ERROR;
    this.numtype = NumType.N_ERROR;
    this.hexFloatExp = false;
    this.decFloatExp = false;

    dec = "";
    mnt = "";
    exp = "";
    suf = "";
    sig = "";

    interpr();
    initSuffixTables();
  }

  public boolean isIntegerKind() {
    if (numtype == NumType.N_ERROR) {
      return false;
    }

    if (numtype == NumType.N_FLOAT || numtype == NumType.N_DOUBLE || numtype == NumType.N_LONG_DOUBLE) {
      return false;
    }

    return true;
  }

  private void initSuffixTables() {
    INT_SUFFIX_TABLE.put("U", NumType.N_UINT);
    INT_SUFFIX_TABLE.put("L", NumType.N_LONG);
    INT_SUFFIX_TABLE.put("UL", NumType.N_ULONG);
    INT_SUFFIX_TABLE.put("LU", NumType.N_ULONG);
    INT_SUFFIX_TABLE.put("LL", NumType.N_LONG_LONG);
    INT_SUFFIX_TABLE.put("ULL", NumType.N_ULONG_LONG);
    INT_SUFFIX_TABLE.put("LLU", NumType.N_ULONG_LONG);

    INT_SUFFIX_TABLE.put("U".toLowerCase(), NumType.N_UINT);
    INT_SUFFIX_TABLE.put("L".toLowerCase(), NumType.N_LONG);
    INT_SUFFIX_TABLE.put("UL".toLowerCase(), NumType.N_ULONG);
    INT_SUFFIX_TABLE.put("LU".toLowerCase(), NumType.N_ULONG);
    INT_SUFFIX_TABLE.put("LL".toLowerCase(), NumType.N_LONG_LONG);
    INT_SUFFIX_TABLE.put("ULL".toLowerCase(), NumType.N_ULONG_LONG);
    INT_SUFFIX_TABLE.put("LLU".toLowerCase(), NumType.N_ULONG_LONG);
  }

  private void interpr() {

    if (size == 0) {
      throw new NumExc("error: empty value");
    }

    CBuf buffer = new CBuf(input);
    int c = buffer.nextc();
    int nextchar = buffer.peekc();

    if (c == '0') {
      if (nextchar == 'x' || nextchar == 'X') {

        setFlag(NumFlagEval.E_HEX);

        buffer.nextc(); // skip [xX]
        for (c = buffer.nextc(); c != HC_FEOF && c != '.' && isHex(c); c = buffer.nextc()) {
          dec += (char) c;
        }

        if (c == '.') {
          setFlag(NumFlagEval.E_HEX_FLOAT);

          for (c = buffer.nextc(); c != HC_FEOF && c != 'p' && c != 'P' && isHex(c); c = buffer.nextc()) {
            mnt += (char) c;
          }
        }

        if (c == 'p' || c == 'P') {
          setHexFloatExp(true);
          setFlag(NumFlagEval.E_HEX_FLOAT);

          c = buffer.nextc();
          if (c == '-') {
            c = buffer.nextc();
            sig = "-";
          } else if (c == '+') {
            c = buffer.nextc();
            sig = "+";
          } else {
            sig = "+";
          }
          for (; c != HC_FEOF && isDec(c); c = buffer.nextc()) {
            exp += (char) c;
          }
        }

        for (; c != HC_FEOF; c = buffer.nextc()) {
          suf += (char) c;
        }

      }

      else if (nextchar == '.') {
        setFlag(NumFlagEval.E_DEC_FLOAT);

        buffer.nextc(); // skip [.]

        dec += "0";

        for (c = buffer.nextc(); c != HC_FEOF && isDec(c); c = buffer.nextc()) {
          mnt += (char) c;
        }

        for (; c != HC_FEOF; c = buffer.nextc()) {
          suf += (char) c;
        }
      }

      else if (nextchar == 'b' || nextchar == 'B') {
        setFlag(NumFlagEval.E_BIN);

        buffer.nextc(); // skip [bB]
        for (c = buffer.nextc(); c != HC_FEOF && isBin(c); c = buffer.nextc()) {
          dec += (char) c;
        }

        for (; c != HC_FEOF; c = buffer.nextc()) {
          suf += (char) c;
        }

      }

      else {

        // in 'c' now first zero
        // we start with get next char, and eat it.
        for (c = buffer.nextc(); c != HC_FEOF && isOct(c); c = buffer.nextc()) {
          dec += (char) c;
        }

        for (; c != HC_FEOF; c = buffer.nextc()) {
          suf += (char) c;
        }

        if (dec.length() > 1) {
          setFlag(NumFlagEval.E_OCT);
        } else {
          // just [0]
          setFlag(NumFlagEval.E_DEC);
        }

      }

    }

    else if (c == '.') {

      setFlag(NumFlagEval.E_DEC_FLOAT);

      for (c = buffer.nextc(); c != HC_FEOF && c != 'e' && c != 'E' && isDec(c); c = buffer.nextc()) {
        mnt += (char) c;
      }

      if (c == 'e' || c == 'E') {
        setDecFloatExp(true);
        setFlag(NumFlagEval.E_DEC_FLOAT);

        c = buffer.nextc();
        if (c == '-') {
          c = buffer.nextc();
          sig = "-";
        } else if (c == '+') {
          c = buffer.nextc();
          sig = "+";
        } else {
          sig = "+";
        }
        for (; c != HC_FEOF && isDec(c); c = buffer.nextc()) {
          exp += (char) c;
        }
      }

      for (; c != HC_FEOF; c = buffer.nextc()) {
        suf += (char) c;
      }

    }

    else /* must be digits [0-9], nothing else. */ {

      setFlag(NumFlagEval.E_DEC);

      if (!isDec(c)) {
        throw new NumExc("error: incorrect numeric constant: [" + input + "]");
      }

      for (; c != HC_FEOF && c != '.' && c != 'e' && c != 'E' && isDec(c); c = buffer.nextc()) {
        dec += (char) c;
      }

      if (c == '.') {
        setFlag(NumFlagEval.E_DEC_FLOAT);

        for (c = buffer.nextc(); c != HC_FEOF && c != 'e' && c != 'E' && isDec(c); c = buffer.nextc()) {
          mnt += (char) c;
        }
      }

      if (c == 'e' || c == 'E') {
        setDecFloatExp(true);
        setFlag(NumFlagEval.E_DEC_FLOAT);

        c = buffer.nextc();
        if (c == '-') {
          c = buffer.nextc();
          sig = "-";
        } else if (c == '+') {
          c = buffer.nextc();
          sig = "+";
        } else {
          sig = "+";
        }
        for (; c != HC_FEOF && isDec(c); c = buffer.nextc()) {
          exp += (char) c;
        }
      }

      for (; c != HC_FEOF; c = buffer.nextc()) {
        suf += (char) c;
      }

    }

  }

  private boolean isInteger() {
    return (flag == NumFlagEval.E_BIN) || (flag == NumFlagEval.E_OCT) || (flag == NumFlagEval.E_DEC)
        || (flag == NumFlagEval.E_HEX);
  }

  private boolean isFloating() {
    return (flag == NumFlagEval.E_DEC_FLOAT) || (flag == NumFlagEval.E_HEX_FLOAT);
  }

  public void ev() {
    checkSuffix();
    checkExponent();

    if (isInteger()) {

      CBuf buffer = new CBuf(dec);
      int base = 10;
      if (flag == NumFlagEval.E_BIN) {
        base = 2;
      }
      if (flag == NumFlagEval.E_OCT) {
        base = 8;
      }
      if (flag == NumFlagEval.E_HEX) {
        base = 16;
      }

      int c = buffer.nextc();

      long retval = 0;
      while (c != HC_FEOF && charCorrectForBase(c, base)) {
        retval = retval * base + cv(base, c);
        c = buffer.nextc();
      }

      setClong(retval);

      if (!suf.isEmpty()) {
        setNumtype(INT_SUFFIX_TABLE.get(suf));
      } else {
        setNumtype(NumType.N_INT);
      }
    }

    if (flag == NumFlagEval.E_HEX_FLOAT) {
      evalhexfloat();
    }

    if (flag == NumFlagEval.E_DEC_FLOAT) {
      evaldecfloat();
    }

  }

  private void setFloating(double realval) {

    NumType resulttype = NumType.N_DOUBLE;
    if (!suf.isEmpty()) {
      if (suf.equals("f") || suf.equals("F")) {
        resulttype = NumType.N_FLOAT;
      } else if (suf.equals("l") || suf.equals("L")) {
        resulttype = NumType.N_LONG_DOUBLE;
      }
    }

    setNumtype(resulttype);

    if (resulttype == NumType.N_LONG_DOUBLE || resulttype == NumType.N_DOUBLE) {
      setCdouble(realval);
    } else {
      setCfloat((float) realval);
    }

  }

  private void evalhexfloat() {

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

    setFloating(realval);

  }

  private void evaldecfloat() {

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

    setFloating(realval);

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

  private void checkSuffix() {
    if (isInteger()) {
      if (!suf.isEmpty()) {
        if (!INT_SUFFIX_TABLE.containsKey(suf)) {
          throw new NumExc("error: incorrect suffif for decimal constant: [" + suf + "]");
        }
      }
    }

    if (isFloating()) {
      if (!suf.isEmpty()) {
        boolean correctFloatSuffix = suf.equals("f") || suf.equals("F") || suf.equals("l") || suf.equals("L");
        if (!correctFloatSuffix) {
          throw new NumExc("error: incorrect suffif for floating constant: [" + suf + "]");
        }
      }
    }
  }

  private void checkExponent() {

    if (isFloating()) {

      // I)
      if (hexFloatExp || decFloatExp) {
        if (exp.isEmpty()) {
          throw new NumExc("error: exponent has no digits: [" + input + "]");
        }
      }

      // II)
      if (flag == NumFlagEval.E_HEX_FLOAT && !hexFloatExp) {
        throw new NumExc("error: hexadecimal floating constant require exponent: [" + input + "]");
      }

    }

  }

  //@formatter:off
  private int cv(int base, int c) {
    
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

  public String getDec() {
    return dec;
  }

  public void setDec(String dec) {
    this.dec = dec;
  }

  public String getMnt() {
    return mnt;
  }

  public void setMnt(String mnt) {
    this.mnt = mnt;
  }

  public String getExp() {
    return exp;
  }

  public void setExp(String exp) {
    this.exp = exp;
  }

  public String getSuf() {
    return suf;
  }

  public void setSuf(String suf) {
    this.suf = suf;
  }

  public String getSig() {
    return sig;
  }

  public void setSig(String sig) {
    this.sig = sig;
  }

  public NumFlagEval getFlag() {
    return flag;
  }

  public void setFlag(NumFlagEval flag) {
    this.flag = flag;
  }

  // UNION
  //
  public long getClong() {
    return clong;
  }

  public float getCfloat() {
    return cfloat;
  }

  public double getCdouble() {
    return cdouble;
  }

  public void setClong(long clong) {
    this.clong = clong;
    this.cfloat = (float) clong;
    this.cdouble = (double) clong;
  }

  public void setCfloat(float cfloat) {
    this.clong = (long) cfloat;
    this.cfloat = cfloat;
    this.cdouble = (double) cfloat;
  }

  public void setCdouble(double cdouble) {
    this.clong = (long) cdouble;
    this.cfloat = (float) cdouble;
    this.cdouble = cdouble;
  }

  public NumType getNumtype() {
    return numtype;
  }

  public void setNumtype(NumType numtype) {
    this.numtype = numtype;
  }

  public boolean isHexFloatExp() {
    return hexFloatExp;
  }

  public void setHexFloatExp(boolean hexFloatExp) {
    this.hexFloatExp = hexFloatExp;
  }

  public boolean isDecFloatExp() {
    return decFloatExp;
  }

  public void setDecFloatExp(boolean decFloatExp) {
    this.decFloatExp = decFloatExp;
  }

}
