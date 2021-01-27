package ast_types;

import static ast_types.TypeBase.TP_BOOLEAN;
import static ast_types.TypeBase.TP_F32;
import static ast_types.TypeBase.TP_F64;
import static ast_types.TypeBase.TP_I16;
import static ast_types.TypeBase.TP_I32;
import static ast_types.TypeBase.TP_I64;
import static ast_types.TypeBase.TP_I8;
import static ast_types.TypeBase.TP_U16;
import static ast_types.TypeBase.TP_U32;
import static ast_types.TypeBase.TP_U64;
import static ast_types.TypeBase.TP_U8;

import java.util.HashMap;
import java.util.Map;

import ast_symtab.IdentMap;
import errors.AstParseException;
import literals.IntLiteral;
import tokenize.Ident;

public abstract class TypeBindings {

  public static final Map<TypeBase, String> BIND_PRIMITIVE_TO_STRING = new HashMap<>();
  static {
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_I8, "i8");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_U8, "u8");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_I16, "i16");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_U16, "u16");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_I32, "i32");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_U32, "u32");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_I64, "i64");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_U64, "u64");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_F32, "f32");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_F64, "f64");
    BIND_PRIMITIVE_TO_STRING.put(TypeBase.TP_BOOLEAN, "boolean");
  }

  //@formatter:off
  
  public static Type make_i8()      { return new Type(TP_I8); }
  public static Type make_u8()      { return new Type(TP_U8); }
  public static Type make_i16()     { return new Type(TP_I16); }
  public static Type make_u16()     { return new Type(TP_U16); }
  public static Type make_i32()     { return new Type(TP_I32); }
  public static Type make_u32()     { return new Type(TP_U32); }
  public static Type make_i64()     { return new Type(TP_I64); }
  public static Type make_u64()     { return new Type(TP_U64); }
  public static Type make_f32()     { return new Type(TP_F32); }
  public static Type make_f64()     { return new Type(TP_F64); }
  public static Type make_boolean() { return new Type(TP_BOOLEAN); }
  
  public static Type getTypeFromIdent(Ident ident) {
         if(ident.equals(IdentMap.i8_ident))      { return make_i8(); }
    else if(ident.equals(IdentMap.u8_ident))      { return make_u8(); }
    else if(ident.equals(IdentMap.i16_ident))     { return make_i16(); }
    else if(ident.equals(IdentMap.u16_ident))     { return make_u16(); }
    else if(ident.equals(IdentMap.i32_ident))     { return make_i32(); }
    else if(ident.equals(IdentMap.u32_ident))     { return make_u32(); }
    else if(ident.equals(IdentMap.i64_ident))     { return make_i64(); }
    else if(ident.equals(IdentMap.u64_ident))     { return make_u64(); }
    else if(ident.equals(IdentMap.f32_ident))     { return make_f32(); }
    else if(ident.equals(IdentMap.f64_ident))     { return make_f64(); }
    else if(ident.equals(IdentMap.boolean_ident)) { return make_boolean(); }
    throw new AstParseException("type not found for ident: " + ident.getName());
  }
  
  public static Type getTypeBySuffix(String suff) {
         if(suff.equals("i8"))  { return make_i8(); }
    else if(suff.equals("u8"))  { return make_u8(); }
    else if(suff.equals("i16")) { return make_i16(); }
    else if(suff.equals("u16")) { return make_u16(); }
    else if(suff.equals("i32")) { return make_i32(); }
    else if(suff.equals("u32")) { return make_u32(); }
    else if(suff.equals("i64")) { return make_i64(); }
    else if(suff.equals("u64")) { return make_u64(); }
    else if(suff.equals("f32")) { return make_f32(); }
    else if(suff.equals("f64")) { return make_f64(); }
    throw new AstParseException("unknown suffix: " + suff.toString());
  }
  
  public static int getPrimitiveTypeSize(Type type) {
         if(type.is(TP_I8))      { return 1; }
    else if(type.is(TP_U8))      { return 1; }
    else if(type.is(TP_I16))     { return 2; }
    else if(type.is(TP_U16))     { return 2; }
    else if(type.is(TP_I32))     { return 4; }
    else if(type.is(TP_U32))     { return 4; }
    else if(type.is(TP_I64))     { return 8; }
    else if(type.is(TP_U64))     { return 8; }
    else if(type.is(TP_F32))     { return 4; } 
    else if(type.is(TP_F64))     { return 8; } // TODO: 
    else if(type.is(TP_BOOLEAN)) { return 1; }
    throw new AstParseException("size not found for type: " + type.toString());
  }
  
  //@formatter:on
}
