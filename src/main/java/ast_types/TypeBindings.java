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
    // if (suff.equals("i8")) {
    //   return make_char(beginPos);
    // } else if (suff.equals("i16")) {
    //   return make_short(beginPos);
    // } else if (suff.equals("i32")) {
    //   return make_int(beginPos);
    // } else if (suff.equals("i64")) {
    //   return make_long(beginPos);
    // } else if (suff.equals("f32")) {
    //   return make_float(beginPos);
    // } else if (suff.equals("f64")) {
    //   return make_double(beginPos);
    // }
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
