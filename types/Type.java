package njast.types;

public class Type {

  //  <type> ::= <primitive type> | <reference type>
  //  
  //  <primitive type> ::= <numeric type> | boolean
  //
  //  <numeric type> ::= <integral type> | <floating-point type>
  //
  //  <integral type> ::= byte | short | int | long | char
  //
  //  <floating-point type> ::= float | double
  //
  //  <reference type> ::= <class or interface type> | <array type>
  //
  //  <class or interface type> ::= <class type> | <interface type>
  //
  //  <class type> ::= <type name>
  //
  //  <interface type> ::= <type name>
  //
  //  <array type> ::= <type> [ ]

  private PrimitiveType primitiveType;
  private ReferenceType referenceType;
  private boolean isPrimitive;

  public Type() {
  }

  public Type(PrimitiveType primitiveType) {
    this.primitiveType = primitiveType;
    this.isPrimitive = true;
  }

  public Type(ReferenceType referenceType) {
    this.referenceType = referenceType;
    this.isPrimitive = false;
  }

  public PrimitiveType getPrimitiveType() {
    return primitiveType;
  }

  public void setPrimitiveType(PrimitiveType primitiveType) {
    this.primitiveType = primitiveType;
  }

  public ReferenceType getReferenceType() {
    return referenceType;
  }

  public void setReferenceType(ReferenceType referenceType) {
    this.referenceType = referenceType;
  }

  public boolean isPrimitive() {
    return isPrimitive;
  }

  public void setPrimitive(boolean isPrimitive) {
    this.isPrimitive = isPrimitive;
  }

  public boolean isEqualTo(Type another) {
    if (isPrimitive) {
      if (!another.isPrimitive()) {
        return false;
      }
      if (!primitiveType.equals(another.getPrimitiveType())) {
        return false;
      }
    } else {
      final String thisStringName = referenceType.getTypeName().getName();
      final String anotStringName = another.getReferenceType().getTypeName().getName();
      if (!thisStringName.equals(anotStringName)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    if (isPrimitive) {
      return primitiveType.toString();
    }
    return referenceType.getTypeName().getName();
  }

}
