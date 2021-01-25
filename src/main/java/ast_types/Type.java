package ast_types;

import static ast_types.TypeBase.TP_ARRAY;
import static ast_types.TypeBase.TP_BOOLEAN;
import static ast_types.TypeBase.TP_CLASS;
import static ast_types.TypeBase.TP_F32;
import static ast_types.TypeBase.TP_F64;
import static ast_types.TypeBase.TP_FUNCTION;
import static ast_types.TypeBase.TP_I16;
import static ast_types.TypeBase.TP_I32;
import static ast_types.TypeBase.TP_I64;
import static ast_types.TypeBase.TP_I8;
import static ast_types.TypeBase.TP_TUPLE;
import static ast_types.TypeBase.TP_TYPE_VARIABLE_TYPENAME_T;
import static ast_types.TypeBase.TP_U16;
import static ast_types.TypeBase.TP_U32;
import static ast_types.TypeBase.TP_U64;
import static ast_types.TypeBase.TP_U8;
import static ast_types.TypeBase.TP_VOID_STUB;

import java.io.Serializable;
import java.util.List;

import ast_checkers.IteratorChecker;
import ast_class.ClassDeclaration;
import errors.AstParseException;
import tokenize.Ident;
import utils_oth.NullChecker;

public class Type implements Serializable, TypeApi {
  private static final long serialVersionUID = -4630043454712001308L;

  private int size;
  private int align;

  private TypeBase base;
  private ClassType classType;
  private Ident typeVariable;
  private ArrayType arrayType;

  public void fillPropValues(Type another) {
    NullChecker.check(another);

    this.base = another.base;
    this.classType = another.classType;
    this.typeVariable = another.typeVariable;
    this.arrayType = another.arrayType;
  }

  public Type() {
    this.base = TypeBase.TP_VOID_STUB;
    this.size = 1;
    this.align = 1;
  }

  public Type(ArrayType array) {
    NullChecker.check(array);

    this.base = TypeBase.TP_ARRAY;
    this.arrayType = array;

    //size
    this.size = arrayType.getCount() * arrayType.getArrayOf().get_size();
    this.align = arrayType.getArrayOf().get_align();
  }

  public Type(TypeBase primitiveType) {
    NullChecker.check(primitiveType);

    if (!is_primitive(primitiveType)) {
      throw new AstParseException("expect primitive type");
    }
    this.base = primitiveType;

    //size
    this.size = TypeBindings.getPrimitiveTypeSize(this);
    this.align = this.size;
  }

  public Type(ClassType ref) {
    NullChecker.check(ref);

    this.base = TypeBase.TP_CLASS;
    this.classType = ref;
  }

  public Type(Ident typeVariable) {
    NullChecker.check(typeVariable);

    this.base = TP_TYPE_VARIABLE_TYPENAME_T;
    this.typeVariable = typeVariable;
  }

  public ArrayType getArrayType() {
    return arrayType;
  }

  public ClassDeclaration getClassType() {
    if (!is_class()) {
      throw new AstParseException("is not a class");
    }
    return classType.getClazz();
  }

  public ClassType getClassTypeRef() {
    return classType;
  }

  public List<Type> getTypeArguments() {
    if (!is_class()) {
      throw new AstParseException("is not a class");
    }
    return classType.getTypeArguments();
  }

  public Ident getTypeVariable() {
    return typeVariable;
  }

  public TypeBase getBase() {
    return base;
  }

  public Ident getTypeParameter() {
    if (!is_type_var()) {
      throw new AstParseException("is not typename T");
    }
    return typeVariable;
  }

  public boolean is_primitive(TypeBase withBase) {
    //@formatter:off
    return withBase == TP_I8
        || withBase == TP_U8
        || withBase == TP_I16
        || withBase == TP_U16
        || withBase == TP_I32
        || withBase == TP_U32
        || withBase == TP_I64
        || withBase == TP_U64
        || withBase == TP_F32
        || withBase == TP_F64
        || withBase == TP_BOOLEAN;
    //@formatter:on
  }

  @Override
  public boolean is_equal_to(Type another) {
    NullChecker.check(another);

    // short: two pointers are equal
    if (this == another) {
      return true;
    }

    if (is(TypeBase.TP_I8)) {
      if (!another.is(TypeBase.TP_I8)) {
        return false;
      }
    } else if (is(TypeBase.TP_U8)) {
      if (!another.is(TypeBase.TP_U8)) {
        return false;
      }
    } else if (is(TypeBase.TP_I16)) {
      if (!another.is(TypeBase.TP_I16)) {
        return false;
      }
    } else if (is(TypeBase.TP_U16)) {
      if (!another.is(TypeBase.TP_U16)) {
        return false;
      }
    } else if (is(TypeBase.TP_I32)) {
      if (!another.is(TypeBase.TP_I32)) {
        return false;
      }
    } else if (is(TypeBase.TP_U32)) {
      if (!another.is(TypeBase.TP_U32)) {
        return false;
      }
    } else if (is(TypeBase.TP_I64)) {
      if (!another.is(TypeBase.TP_I64)) {
        return false;
      }
    } else if (is(TypeBase.TP_U64)) {
      if (!another.is(TypeBase.TP_U64)) {
        return false;
      }
    } else if (is(TypeBase.TP_F32)) {
      if (!another.is(TypeBase.TP_F32)) {
        return false;
      }
    } else if (is(TypeBase.TP_F64)) {
      if (!another.is(TypeBase.TP_F64)) {
        return false;
      }
    } else if (is(TypeBase.TP_BOOLEAN)) {
      if (!another.is(TypeBase.TP_BOOLEAN)) {
        return false;
      }
    } else if (is(TypeBase.TP_VOID_STUB)) {
      if (!another.is(TypeBase.TP_VOID_STUB)) {
        return false;
      }
    } else if (is(TypeBase.TP_TYPE_VARIABLE_TYPENAME_T)) {
      if (!another.is(TypeBase.TP_TYPE_VARIABLE_TYPENAME_T)) {
        return false;
      }
      final Ident anotherTypeVariable = another.getTypeVariable();
      final String name1 = typeVariable.getName();
      final String name2 = anotherTypeVariable.getName();
      if (!name1.equals(name2)) {
        return false;
      }
    } else if (is(TypeBase.TP_CLASS)) {
      if (!another.is(TypeBase.TP_CLASS)) {
        return false;
      }
      if (!classType.is_equal_to(another.getClassTypeRef())) {
        return false;
      }
    } else if (is(TypeBase.TP_FUNCTION)) {
      if (!another.is(TypeBase.TP_FUNCTION)) {
        return false;
      }
    } else if (is(TypeBase.TP_ARRAY)) {
      if (!another.is(TypeBase.TP_ARRAY)) {
        return false;
      }
      final ArrayType anotherArray = another.getArrayType();
      if (!arrayType.is_equal_to(anotherArray)) {
        return false;
      }
    } else if (is(TypeBase.TP_TUPLE)) {
      if (!another.is(TypeBase.TP_TUPLE)) {
        return false;
      }
    } else {
      throw new AstParseException("unimplemented comparison for base: " + base.toString());
    }

    return true;

  }

  @Override
  public String toString() {
    if (is_primitive()) {
      return TypeBindings.BIND_PRIMITIVE_TO_STRING.get(base);
    }
    if (is_type_var()) {
      return typeVariable.getName();
    }
    if (is_void_stub()) {
      return "void";
    }
    if (is_array()) {
      if (arrayType == null) {
        return "[???]";
      }
      return arrayType.toString();
    }
    if (is_class()) {
      return classType.toString();
    }
    return base.toString();
  }

  public boolean is(TypeBase withBase) {
    return this.base.equals(withBase);
  }

  private boolean isOneOf(TypeBase withBase[]) {
    for (TypeBase item : withBase) {
      if (is(item)) {
        return true;
      }
    }
    return false;
  }

  //@formatter:off
  @Override public boolean is_i8()        { return is(TP_I8); }
  @Override public boolean is_u8()        { return is(TP_U8); }
  @Override public boolean is_i16()       { return is(TP_I16); }
  @Override public boolean is_u16()       { return is(TP_U16); }
  @Override public boolean is_i32()       { return is(TP_I32); }
  @Override public boolean is_u32()       { return is(TP_U32); }
  @Override public boolean is_i64()       { return is(TP_I64); }
  @Override public boolean is_u64()       { return is(TP_U64); }
  @Override public boolean is_f32()       { return is(TP_F32); }
  @Override public boolean is_f64()       { return is(TP_F64); }
  @Override public boolean is_boolean()   { return is(TP_BOOLEAN); }
  @Override public boolean is_void_stub() { return is(TP_VOID_STUB); }
  @Override public boolean is_function()  { return is(TP_FUNCTION); }
  @Override public boolean is_array()     { return is(TP_ARRAY); }
  @Override public boolean is_tuple()     { return is(TP_TUPLE); }
  //@formatter:on

  @Override
  public boolean is_primitive() {
    return is_primitive(base);
  }

  @Override
  public boolean is_type_var() {
    return is(TypeBase.TP_TYPE_VARIABLE_TYPENAME_T);
  }

  @Override
  public boolean is_class() {
    return is(TP_CLASS);
  }

  @Override
  public boolean is_class_template() {
    return is_class() && classType.isTemplate();
  }

  @Override
  public int get_size() {
    return size;
  }

  @Override
  public int get_align() {
    return align;
  }

  @Override
  public boolean is_iterated() {
    final IteratorChecker checker = new IteratorChecker(this);
    return checker.isIterable();
  }

  @Override
  public boolean is_reference() {
    return is_class() || is_array();
  }

  @Override
  public boolean is_has_signedness() {
    return false;
  }

  @Override
  public boolean is_signed() {
    return false;
  }

  @Override
  public boolean is_unsigned() {
    return false;
  }

  @Override
  public boolean is_numeric() {
    return is_integer() || is_floating();
  }

  @Override
  public boolean is_integer() {
    TypeBase bases[] = { TP_I8, TP_U8, TP_I16, TP_U16, TP_I32, TP_U32, TP_I64, TP_U64 };
    return isOneOf(bases);
  }

  @Override
  public boolean is_floating() {
    return is(TP_F32) || is(TypeBase.TP_F64);
  }

  public void set_size(int i) {
    this.size = i;
  }
}
