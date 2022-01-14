package ast_symtab;

import hashed.Hash_ident;
import tokenize.Ident;

public final class Keywords {

  /// you may put here ONLY keywords... nothings else...
  /// each field will be processed by reflection at ParserMain
  /// initialization, and it will be formed as an
  /// keyword in a symbol-table.

  private static Ident g(String name) {
    return Hash_ident.getHashedIdent(name, 32);
  }

  //@formatter:off
  
  public static final Ident break_ident          = g("break");
  public static final Ident continue_ident       = g("continue");
  public static final Ident do_ident             = g("do");
  public static final Ident else_ident           = g("else");
  public static final Ident for_ident            = g("for");
  public static final Ident if_ident             = g("if");
  public static final Ident implements_ident     = g("implements");
  public static final Ident import_ident         = g("import");
  public static final Ident new_ident            = g("new");
  public static final Ident return_ident         = g("return");
  public static final Ident while_ident          = g("while");
  
  // modifiers
  public static final Ident native_ident         = g("native");
  public static final Ident static_ident         = g("static");
  public static final Ident final_ident          = g("final");
  public static final Ident private_ident        = g("private");
  public static final Ident public_ident         = g("public");
  
  // literals
  public static final Ident true_ident           = g("true");
  public static final Ident false_ident          = g("false");
  public static final Ident this_ident           = g("this");
  public static final Ident default_ident        = g("default");
  
  // new syntax:
  public static final Ident cast_ident           = g("cast");
  public static final Ident sizeof_ident         = g("sizeof");
  public static final Ident deinit_ident         = g("deinit");
  public static final Ident test_ident           = g("test");
  
  // global funcs
  public static final Ident static_assert_ident  = g("static_assert");
  public static final Ident assert_true_ident    = g("assert_true");
  
  public static final Ident is_alive_ident            = g("is_alive");
  public static final Ident set_deletion_bit_ident    = g("set_deletion_bit");
  
  // types:
  public static final Ident char_ident           = g("char");
  public static final Ident short_ident          = g("short");
  public static final Ident int_ident            = g("int");
  public static final Ident long_ident           = g("long");
  public static final Ident float_ident          = g("float");
  public static final Ident double_ident         = g("double");
  public static final Ident boolean_ident        = g("boolean");
  public static final Ident void_ident           = g("void");
  public static final Ident class_ident          = g("class");
  public static final Ident interface_ident      = g("interface");
  public static final Ident enum_ident           = g("enum");
  
  // type-traits
  public static final Ident is_void_ident        = g("is_void");
  public static final Ident is_boolean_ident     = g("is_boolean");
  public static final Ident is_char_ident        = g("is_char");
  //public static final Ident is_uchar_ident       = g("is_uchar");
  public static final Ident is_short_ident       = g("is_short");
  //public static final Ident is_ushort_ident      = g("is_ushort");
  public static final Ident is_int_ident         = g("is_int");
  //public static final Ident is_uint_ident        = g("is_uint");
  public static final Ident is_long_ident        = g("is_long");
  //public static final Ident is_ulong_ident       = g("is_ulong");
  public static final Ident is_float_ident       = g("is_float");
  public static final Ident is_double_ident      = g("is_double");
  public static final Ident is_integral_ident    = g("is_integral");
  public static final Ident is_floating_ident    = g("is_floating");
  //public static final Ident is_function_ident    = g("is_function");
  public static final Ident is_class_ident       = g("is_class");
  public static final Ident is_interface_ident   = g("is_interface");
  //public static final Ident is_enum_ident        = g("is_enum");
  public static final Ident is_primitive_ident   = g("is_primitive");
  public static final Ident is_arithmetic_ident  = g("is_arithmetic");
  public static final Ident is_reference_ident   = g("is_reference");
  //public static final Ident is_signed_ident      = g("is_signed");
  //public static final Ident is_unsigned_ident    = g("is_unsigned");
  public static final Ident types_are_same_ident = g("types_are_same");
  
  //@formatter:on

}
