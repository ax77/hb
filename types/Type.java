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

  public Type() {
  }

  public Type(PrimitiveType primitiveType) {
    this.primitiveType = primitiveType;
  }

  public Type(ReferenceType referenceType) {
    this.referenceType = referenceType;
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

}
