package njast.types;

import java.io.Serializable;

import jscan.symtab.Ident;
import njast.errors.EParseException;

public class Type implements Serializable {
  private static final long serialVersionUID = -4630043454712001308L;

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

  public boolean isTypeParameterStub() {
    return base == TypeBase.REFERENCE && referenceType.getBase() == ReferenceTypeBase.TYPE_VARIABLE_T;
  }

  public Ident getTypeParameter() {
    if (!isTypeParameterStub()) {
      throw new EParseException("is not typename T");
    }
    return referenceType.getTypeVariable();
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

  public boolean isEqualTo(Type another) {
    if (isPrimitive()) {
      if (!another.isPrimitive()) {
        return false;
      }
      if (!primitiveType.equals(another.getPrimitiveType())) {
        return false;
      }
    } else if (isReference()) {
      final Ident name1 = referenceType.getClassType().getIdentifier();
      final Ident name2 = another.getReferenceType().getClassType().getIdentifier();
      if (!name1.equals(name2)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    if (isPrimitive()) {

      // TP_BYTE, TP_SHORT, TP_CHAR, TP_INT, TP_LONG, TP_FLOAT, TP_DOUBLE, TP_BOOLEAN
      if (primitiveType.equals(PrimitiveType.TP_INT)) {
        return "int";
      }

      return primitiveType.toString();
    }
    return referenceType.toString();
  }

}
