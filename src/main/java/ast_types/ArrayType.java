package ast_types;

import java.io.Serializable;

import ast_st1_templates.TypeSetter;

public class ArrayType implements Serializable, TypeSetter {
  private static final long serialVersionUID = 3421706133636460085L;

  private /*final*/ Type arrayOf;
  private int count;

  public ArrayType(Type arrayOf, int count) {
    this.arrayOf = arrayOf;
    this.count = count;
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
    final Type sub1 = getType();
    final Type sub2 = another.getType();
    if (!sub1.is_equal_to(sub2)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    if (count > 0) {
      sb.append(String.format("%d", count));
      sb.append(": ");
    }
    sb.append(arrayOf.toString());
    sb.append("]");
    return sb.toString();
  }

  @Override
  public void setType(Type typeToSet) {
    this.arrayOf = typeToSet;
  }

  @Override
  public Type getType() {
    return arrayOf;
  }

}
