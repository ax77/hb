package ast_types;

import java.io.Serializable;

import _st1_templates.TypeSetter;

/// why do we need this special type?
/// why not an array itelf like: [u8]?
/// because of the incapsulation
/// while I'm not sure about the memory model
/// it is easy to manage arrays inside two
/// classes: 'array<T>' and 'string'
/// we can actually paste 'new array<u8>()'
/// instead of each declaration like 'new [2: u8]'
/// and also we can paste 'arr.get(i)' 
/// instead of each arr[i], and so on.
/// but it is a syntax sugar, and means nothing now.
/// for strings we have to create an fixed-sized array
/// of 'u8', and manage it inside string class itself
/// we know when we have to destroy the array, and we
/// can manipulate with it easily...
/// the design might change later, but by now I don't
/// want to create a huge class like 'EmbeddedArray', 
/// which will be treated as an 'array of objects',
/// hello 'pointer to void'...
/// no: each array is strong-typed, and its lifetime
/// is obvious.
///

public class TypeBuiltinArray implements Serializable, TypeSetter {
  private static final long serialVersionUID = 2597681550464877019L;

  private Type elementType;

  public TypeBuiltinArray(Type elementType) {
    this.elementType = elementType;
  }

  @Override
  public String toString() {
    return elementType.toString() + "[]";
    // return "builtin.array_declare(" + elementType.toString() + ")";
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
