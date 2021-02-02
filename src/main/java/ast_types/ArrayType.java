package ast_types;

import java.io.Serializable;

import ast_st1_templates.TypeSetter;

public class ArrayType implements Serializable, TypeSetter {
  private static final long serialVersionUID = 3421706133636460085L;

  private /*final*/ Type elementType;
  private int length;

  public ArrayType(Type elementType, int length) {
    this.elementType = elementType;
    this.length = length;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int count) {
    this.length = count;
  }

  public boolean is_equal_to(ArrayType another) {
    if (this == another) {
      return true;
    }

    if (getLength() != another.getLength()) {
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
    if (length > 0) {
      sb.append(String.format("%d", length));
      sb.append(": ");
    }
    sb.append(elementType.toString());
    sb.append("]");
    return sb.toString();
  }

  @Override
  public void setType(Type typeToSet) {
    this.elementType = typeToSet;
  }

  @Override
  public Type getType() {
    return elementType;
  }

}
