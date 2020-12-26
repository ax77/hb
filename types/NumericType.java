package njast.types;

public class NumericType {

  private IntegralType integralType;

  public NumericType(IntegralType tpInt) {
    this.integralType = tpInt;
  }

  public NumericType() {
  }

  public IntegralType getIntegralType() {
    return integralType;
  }

  public void setIntegralType(IntegralType integralType) {
    this.integralType = integralType;
  }

}
