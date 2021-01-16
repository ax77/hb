package njast.ast.nodes.expr;

import java.io.Serializable;

import jscan.cstrtox.NumType;

public class ExprNumber implements Serializable {
  private static final long serialVersionUID = 4121213191687917298L;

  private final NumType numtype;
  private final long clong;
  private final double cdouble;

  public ExprNumber(long clong, NumType numtype) {
    this.numtype = numtype;
    this.clong = clong;
    this.cdouble = (double) clong;
  }

  public ExprNumber(double cdouble, NumType numtype) {
    this.numtype = numtype;
    this.clong = (long) cdouble;
    this.cdouble = cdouble;
  }

  public long getClong() {
    return clong;
  }

  public double getCdouble() {
    return cdouble;
  }

  public NumType getNumtype() {
    return numtype;
  }

  public boolean isInteger() {
    if (numtype == NumType.N_ERROR) {
      return false;
    }
    if (numtype == NumType.N_FLOAT || numtype == NumType.N_DOUBLE || numtype == NumType.N_LONG_DOUBLE) {
      return false;
    }
    return true;
  }

}
