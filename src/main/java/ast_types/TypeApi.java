package ast_types;

public interface TypeApi {
  //@formatter:off
  public boolean isChar();
  public boolean isShort();
  public boolean isInt();
  public boolean isLong();
  public boolean isFloat();
  public boolean isDouble();
  public boolean isBoolean();
  public boolean isVoid();
  public boolean isTypenameID();
  public boolean isClass();
  public int     getSize();
  public int     getAlign();
  public boolean isEqualTo(Type another);
  public boolean isClassTemplate();
  public boolean isPrimitive();
  public boolean isNumeric();
  public boolean isInteger();
  public boolean isFloating();
  public boolean isStdPointer();
}
