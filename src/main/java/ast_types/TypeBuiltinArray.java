package ast_types;

public class TypeBuiltinArray {
  private final Type elementType;

  public TypeBuiltinArray(Type elementType) {
    this.elementType = elementType;
  }

  public Type getElementType() {
    return elementType;
  }

  @Override
  public String toString() {
    return "builtin.array_declare(" + elementType.toString() + ")";
  }

}
