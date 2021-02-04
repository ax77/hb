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

import ast_symtab.Keywords;
import errors.AstParseException;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

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
  
  public static Type make_i8 (Token beginPos)     { return new Type(TP_I8      , beginPos); }
  public static Type make_u8 (Token beginPos)     { return new Type(TP_U8      , beginPos); }
  public static Type make_i16(Token beginPos)     { return new Type(TP_I16     , beginPos); }
  public static Type make_u16(Token beginPos)     { return new Type(TP_U16     , beginPos); }
  public static Type make_i32(Token beginPos)     { return new Type(TP_I32     , beginPos); }
  public static Type make_u32(Token beginPos)     { return new Type(TP_U32     , beginPos); }
  public static Type make_i64(Token beginPos)     { return new Type(TP_I64     , beginPos); }
  public static Type make_u64(Token beginPos)     { return new Type(TP_U64     , beginPos); }
  public static Type make_f32(Token beginPos)     { return new Type(TP_F32     , beginPos); }
  public static Type make_f64(Token beginPos)     { return new Type(TP_F64     , beginPos); }
  public static Type make_boolean(Token beginPos) { return new Type(TP_BOOLEAN , beginPos); }
  
  public static Type getTypeFromIdent(Token tok) {
    if(!tok.ofType(T.TOKEN_IDENT)) {
      throw new AstParseException("expected identifier, but was: " + tok.getValue());
    }
    Ident ident = tok.getIdent();
         if(ident.equals(Keywords.i8_ident))      { return make_i8 (tok); }
    else if(ident.equals(Keywords.u8_ident))      { return make_u8 (tok); }
    else if(ident.equals(Keywords.i16_ident))     { return make_i16(tok); }
    else if(ident.equals(Keywords.u16_ident))     { return make_u16(tok); }
    else if(ident.equals(Keywords.i32_ident))     { return make_i32(tok); }
    else if(ident.equals(Keywords.u32_ident))     { return make_u32(tok); }
    else if(ident.equals(Keywords.i64_ident))     { return make_i64(tok); }
    else if(ident.equals(Keywords.u64_ident))     { return make_u64(tok); }
    else if(ident.equals(Keywords.f32_ident))     { return make_f32(tok); }
    else if(ident.equals(Keywords.f64_ident))     { return make_f64(tok); }
    else if(ident.equals(Keywords.boolean_ident)) { return make_boolean(tok); }
    throw new AstParseException("type not found for ident: " + ident.getName());
  }
  
  public static Type getTypeBySuffix(String suff, Token beginPos) {
         if(suff.equals("i8"))  { return make_i8 (beginPos); }
    else if(suff.equals("u8"))  { return make_u8 (beginPos); }
    else if(suff.equals("i16")) { return make_i16(beginPos); }
    else if(suff.equals("u16")) { return make_u16(beginPos); }
    else if(suff.equals("i32")) { return make_i32(beginPos); }
    else if(suff.equals("u32")) { return make_u32(beginPos); }
    else if(suff.equals("i64")) { return make_i64(beginPos); }
    else if(suff.equals("u64")) { return make_u64(beginPos); }
    else if(suff.equals("f32")) { return make_f32(beginPos); }
    else if(suff.equals("f64")) { return make_f64(beginPos); }
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
