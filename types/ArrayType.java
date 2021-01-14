package njast.types;

import java.io.Serializable;

public class ArrayType implements Serializable {
  private static final long serialVersionUID = 3421706133636460085L;

  private final Type arrayOf;
  private final long count;

  public ArrayType(Type arrayOf, long count) {
    this.arrayOf = arrayOf;
    this.count = count;
  }

  public Type getArrayOf() {
    return arrayOf;
  }

  public long getCount() {
    return count;
  }

  @Override
  public String toString() {
    return "[" + String.format("%d", count) + ": " + arrayOf.toString() + "]";
  }

}
