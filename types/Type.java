package njast.types;

import jscan.symtab.Ident;

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

  public final static Type BYTE_TYPE = new Type(PrimitiveType.TP_BYTE);
  public final static Type SHORT_TYPE = new Type(PrimitiveType.TP_SHORT);
  public final static Type CHAR_TYPE = new Type(PrimitiveType.TP_CHAR);
  public final static Type INT_TYPE = new Type(PrimitiveType.TP_INT);
  public final static Type LONG_TYPE = new Type(PrimitiveType.TP_LONG);
  public final static Type FLOAT_TYPE = new Type(PrimitiveType.TP_FLOAT);
  public final static Type DOUBLE_TYPE = new Type(PrimitiveType.TP_DOUBLE);
  public final static Type BOOLEAN_TYPE = new Type(PrimitiveType.TP_BOOLEAN);

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
      final Ident name1 = referenceType.getTypeName().getIdentifier();
      final Ident name2 = another.getReferenceType().getTypeName().getIdentifier();
      if (!name1.equals(name2)) {
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
    return referenceType.toString();
  }

}
