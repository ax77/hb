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
  
  public static final Ident abstract_ident       = g("abstract");
  public static final Ident break_ident          = g("break");
  public static final Ident catch_ident          = g("catch");
  public static final Ident continue_ident       = g("continue");
  public static final Ident do_ident             = g("do");
  public static final Ident else_ident           = g("else");
  public static final Ident enum_ident           = g("enum");
  public static final Ident final_ident          = g("final");
  public static final Ident for_ident            = g("for");
  public static final Ident if_ident             = g("if");
  public static final Ident implements_ident     = g("implements");
  public static final Ident import_ident         = g("import");
  public static final Ident instanceof_ident     = g("instanceof");
  public static final Ident interface_ident      = g("interface");
  public static final Ident native_ident         = g("native");
  public static final Ident new_ident            = g("new");
  public static final Ident package_ident        = g("package");
  public static final Ident private_ident        = g("private");
  public static final Ident public_ident         = g("public");
  public static final Ident return_ident         = g("return");
  public static final Ident static_ident         = g("static");
  public static final Ident while_ident          = g("while");
  // literals
  public static final Ident null_ident           = g("null");
  public static final Ident true_ident           = g("true");
  public static final Ident false_ident          = g("false");
  // new syntax:
  public static final Ident var_ident            = g("var");
  public static final Ident let_ident            = g("let");
  public static final Ident weak_ident           = g("weak");
  public static final Ident init_ident           = g("init");
  public static final Ident deinit_ident         = g("deinit");
  public static final Ident self_ident           = g("self");
  public static final Ident in_ident             = g("in");
  public static final Ident cast_ident           = g("cast");
  // types:
  public static final Ident i8_ident             = g("i8");
  public static final Ident u8_ident             = g("u8");
  public static final Ident i16_ident            = g("i16");
  public static final Ident u16_ident            = g("u16");
  public static final Ident i32_ident            = g("i32");
  public static final Ident u32_ident            = g("u32");
  public static final Ident i64_ident            = g("i64");
  public static final Ident u64_ident            = g("u64");
  public static final Ident f32_ident            = g("f32");
  public static final Ident f64_ident            = g("f64");
  public static final Ident boolean_ident        = g("boolean");
  public static final Ident void_ident           = g("void");
  public static final Ident class_ident          = g("class");
  public static final Ident func_ident           = g("func");
  
  //@formatter:on

}
