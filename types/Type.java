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

  private final TypeBase base;
  private PrimitiveType primitiveType;
  private ReferenceType referenceType;
  private Ident typeParameter;

  public final static Type BYTE_TYPE = new Type(PrimitiveType.TP_BYTE);
  public final static Type SHORT_TYPE = new Type(PrimitiveType.TP_SHORT);
  public final static Type CHAR_TYPE = new Type(PrimitiveType.TP_CHAR);
  public final static Type INT_TYPE = new Type(PrimitiveType.TP_INT);
  public final static Type LONG_TYPE = new Type(PrimitiveType.TP_LONG);
  public final static Type FLOAT_TYPE = new Type(PrimitiveType.TP_FLOAT);
  public final static Type DOUBLE_TYPE = new Type(PrimitiveType.TP_DOUBLE);
  public final static Type BOOLEAN_TYPE = new Type(PrimitiveType.TP_BOOLEAN);

  public Type(PrimitiveType primitiveType) {
    this.base = TypeBase.PRIMITIVE;
    this.primitiveType = primitiveType;
  }

  public Type(ReferenceType referenceType) {
    this.base = TypeBase.REFERENCE;
    this.referenceType = referenceType;
  }

  public Type(Ident typeParameter) {
    this.base = TypeBase.TYPE_PARAMETER_STUB;
    this.typeParameter = typeParameter;
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
    return base == TypeBase.PRIMITIVE;
  }

  public boolean isReference() {
    return base == TypeBase.REFERENCE;
  }

  public boolean isTypeParameterStub() {
    return base == TypeBase.TYPE_PARAMETER_STUB;
  }

  public Ident getTypeParameter() {
    return typeParameter;
  }

  public boolean isEqualTo(Type another) {
    if (isPrimitive()) {
      if (!another.isPrimitive()) {
        return false;
      }
      if (!primitiveType.equals(another.getPrimitiveType())) {
        return false;
      }
    } else if (isReference()) {
      final Ident name1 = referenceType.getTypeName().getIdentifier();
      final Ident name2 = another.getReferenceType().getTypeName().getIdentifier();
      if (!name1.equals(name2)) {
        return false;
      }
    } else {
      if (!typeParameter.equals(another.getTypeParameter())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    if (isPrimitive()) {
      return primitiveType.toString();
    } else if (isReference()) {
      return referenceType.toString();
    }
    return "T:" + typeParameter.getName();
  }

}
