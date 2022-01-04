package ast_types;

import static ast_types.TypeBase.TP_boolean;
import static ast_types.TypeBase.TP_char;
import static ast_types.TypeBase.TP_double;
import static ast_types.TypeBase.TP_float;
import static ast_types.TypeBase.TP_int;
import static ast_types.TypeBase.TP_long;
import static ast_types.TypeBase.TP_short;

import java.util.HashMap;
import java.util.Map;

import ast_symtab.Keywords;
import errors.AstParseException;
import tokenize.Ident;

public abstract class TypeBindings {

  public static final Map<TypeBase, String> BIND_PRIMITIVE_TO_STRING = new HashMap<>();
  static {
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_char, "char");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_short, "short");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_int, "int");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_long, "long");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_float, "float");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_double, "double");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_boolean, "boolean");
  }

  public static int getResultForTypeTraits(Ident typeTraitsId, Type tp) {

    //@formatter:off
    
    if (typeTraitsId.equals(Keywords.is_void_ident))       { if (tp.isVoid()) { return 1; } return 0; }
    if (typeTraitsId.equals(Keywords.is_boolean_ident))    { if (tp.isBoolean()) { return 1; } return 0; }
    if (typeTraitsId.equals(Keywords.is_char_ident))       { if (tp.isChar()) { return 1; } return 0; }
    if (typeTraitsId.equals(Keywords.is_short_ident))      { if (tp.isShort()) { return 1; } return 0; }
    if (typeTraitsId.equals(Keywords.is_int_ident))        { if (tp.isInt()) { return 1; } return 0; }
    if (typeTraitsId.equals(Keywords.is_long_ident))       { if (tp.isLong()) { return 1; } return 0; }
    if (typeTraitsId.equals(Keywords.is_float_ident))      { if (tp.isFloat()) { return 1; } return 0; }
    if (typeTraitsId.equals(Keywords.is_double_ident))     { if (tp.isDouble()) { return 1; } return 0; }
    if (typeTraitsId.equals(Keywords.is_integral_ident))   { if (tp.isInteger()) { return 1; } return 0; }
    if (typeTraitsId.equals(Keywords.is_floating_ident))   { if (tp.isFloating()) { return 1; } return 0; }
    if (typeTraitsId.equals(Keywords.is_class_ident))      { if (tp.isClass()) { return 1; } return 0; }
    if (typeTraitsId.equals(Keywords.is_primitive_ident))  { if (tp.isPrimitive()) { return 1; } return 0; }
    if (typeTraitsId.equals(Keywords.is_arithmetic_ident)) { if (tp.isArithmetic()) { return 1; } return 0; } // TODO:rename
    if (typeTraitsId.equals(Keywords.is_reference_ident))  { if (tp.isClass()) { return 1; } return 0; } // TODO:class+enum+interface
    
    //@formatter:on

    return -1;
  }

  public static Type make_char() {
    return new Type(TP_char);
  }

  public static Type make_short() {
    return new Type(TP_short);
  }

  public static Type make_int() {
    return new Type(TP_int);
  }

  public static Type make_long() {
    return new Type(TP_long);
  }

  public static Type make_float() {
    return new Type(TP_float);
  }

  public static Type make_double() {
    return new Type(TP_double);
  }

  public static Type make_boolean() {
    return new Type(TP_boolean);
  }

  public static Type getTypeFromIdent(Ident ident) {
    if (ident.equals(Keywords.char_ident)) {
      return make_char();
    } else if (ident.equals(Keywords.short_ident)) {
      return make_short();
    } else if (ident.equals(Keywords.int_ident)) {
      return make_int();
    } else if (ident.equals(Keywords.long_ident)) {
      return make_long();
    } else if (ident.equals(Keywords.float_ident)) {
      return make_float();
    } else if (ident.equals(Keywords.double_ident)) {
      return make_double();
    } else if (ident.equals(Keywords.boolean_ident)) {
      return make_boolean();
    }
    throw new AstParseException("type not found for ident: " + ident.getName());
  }

  public static Type getTypeBySuffix(String suff) {
    if (suff.equals("i8")) {
      return make_char();
    } else if (suff.equals("i16")) {
      return make_short();
    } else if (suff.equals("i32")) {
      return make_int();
    } else if (suff.equals("i64")) {
      return make_long();
    } else if (suff.equals("f32")) {
      return make_float();
    } else if (suff.equals("f64")) {
      return make_double();
    }
    throw new AstParseException("unknown suffix: " + suff.toString());
  }

  public static int getPrimitiveTypeSize(Type type) {
    if (type.is(TP_char)) {
      return 1;
    } else if (type.is(TP_short)) {
      return 2;
    } else if (type.is(TP_int)) {
      return 4;
    } else if (type.is(TP_long)) {
      return 8;
    } else if (type.is(TP_float)) {
      return 4;
    } else if (type.is(TP_double)) {
      return 8;
    } else if (type.is(TP_boolean)) {
      return 1;
    }
    throw new AstParseException("size not found for type: " + type.toString());
  }

}
