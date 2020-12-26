package njast.types;

public class PrimitiveType {

  private NumericType numericType;
  private boolean isBoolean;

  public PrimitiveType() {
    this.isBoolean = true;
  }

  public PrimitiveType(NumericType numericType) {
    this.numericType = numericType;
    this.isBoolean = false;
  }

  public NumericType getNumericType() {
    return numericType;
  }

  public void setNumericType(NumericType numericType) {
    this.numericType = numericType;
  }

  public boolean isBoolean() {
    return isBoolean;
  }

  public void setBoolean(boolean isBoolean) {
    this.isBoolean = isBoolean;
  }

}
