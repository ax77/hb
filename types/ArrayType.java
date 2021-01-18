package njast.types;

import java.io.Serializable;

public class ArrayType implements Serializable {
  private static final long serialVersionUID = 3421706133636460085L;

  private final Type arrayOf;
  private int count;

  public ArrayType(Type arrayOf, int count) {
    this.arrayOf = arrayOf;
    this.count = count;
  }

  public Type getArrayOf() {
    return arrayOf;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  @Override
  public String toString() {
    return "[" + String.format("%d", count) + ": " + arrayOf.toString() + "]";
  }

}
