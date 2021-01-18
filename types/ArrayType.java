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

  public boolean is_equal_to(ArrayType another) {
    if (this == another) {
      return true;
    }

    if (getCount() != another.getCount()) {
      return false;
    }
    final Type sub1 = getArrayOf();
    final Type sub2 = another.getArrayOf();
    if (!sub1.is_equal_to(sub2)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "[" + String.format("%d", count) + ": " + arrayOf.toString() + "]";
  }

}
