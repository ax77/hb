package literals;

import java.io.Serializable;
import java.util.Locale;

import _st3_linearize_expr.CEscaper;
import ast_types.Type;

public class IntLiteral implements Serializable {
  private static final long serialVersionUID = 7055604293623516324L;

  private Type type;
  private final String originalInput;
  private final char mainSign;
  private final String dec;
  private final String mnt;
  private final String exp;
  private final String suf;
  private final char exponentSign;

  private int intValue;
  private long longValue;
  private float floatValue;
  private double doubleValue;

  public IntLiteral(String originalInput, char mainSign, String dec, String mnt, String exp, String suf,
      char exponentSign) {
    this.originalInput = originalInput;
    this.mainSign = mainSign;
    this.dec = dec;
    this.mnt = mnt;
    this.exp = exp;
    this.suf = suf;
    this.exponentSign = exponentSign;
  }

  public void setInt(int n) {
    this.intValue = (int) n;
    this.longValue = (long) n;
    this.floatValue = (float) n;
    this.doubleValue = (double) n;
  }

  public void setLong(long n) {
    this.intValue = (int) n;
    this.longValue = (long) n;
    this.floatValue = (float) n;
    this.doubleValue = (double) n;
  }

  public void setFloat(float n) {
    this.intValue = (int) n;
    this.longValue = (long) n;
    this.floatValue = (float) n;
    this.doubleValue = (double) n;
  }

  public void setDouble(double n) {
    this.intValue = (int) n;
    this.longValue = (long) n;
    this.floatValue = (float) n;
    this.doubleValue = (double) n;
  }

  public String getOriginalInput() {
    return originalInput;
  }

  public char getMainSign() {
    return mainSign;
  }

  public String getDec() {
    return dec;
  }

  public String getMnt() {
    return mnt;
  }

  public String getExp() {
    return exp;
  }

  public String getSuf() {
    return suf;
  }

  public char getExponentSign() {
    return exponentSign;
  }

  public int getIntValue() {
    return intValue;
  }

  public long getLongValue() {
    return longValue;
  }

  public float getFloatValue() {
    return floatValue;
  }

  public double getDoubleValue() {
    return doubleValue;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public Type getType() {
    return type;
  }

  @Override
  public String toString() {
    if (type.isFloating()) {
      //return String.format("%f", floatValue);
      return String.format(Locale.US, "%f", floatValue);
    }
    if (type.isChar()) {
      final String res = String.format("'%s'", CEscaper.unesc((char) intValue));
      return res;
    }
    final String res = String.format("%d", intValue);
    return res;
  }

  //  @Override
  //  public String toString() {
  //    String expSig = exp.length() == 0 ? "" : Character.toString(exponentSign);
  //    return mainSign + dec.toString() + "|" + mnt.toString() + "|" + expSig + exp.toString() + "|" + suf.toString();
  //  }

  // A - evaluate as a decimal integer (bin, oct, dec, hex) -> 0b_1111_1111, 0o377, 255, 0xFF
  // f - evaluate as a floating (in its decimal form) -> 3.766280e-04, 3.14, .2003
  // F - evaluate as a floating (in its hex form) -> 0x1.e63674fa06bc9p+18
}