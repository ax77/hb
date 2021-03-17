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
  public static final Ident enum_ident           = g("enum");
  public static final Ident final_ident          = g("final");
  public static final Ident for_ident            = g("for");
  public static final Ident if_ident             = g("if");
  public static final Ident implements_ident     = g("implements");
  public static final Ident import_ident         = g("import");
  public static final Ident interface_ident      = g("interface");
  public static final Ident native_ident         = g("native");
  public static final Ident new_ident            = g("new");
  public static final Ident private_ident        = g("private");
  public static final Ident public_ident         = g("public");
  public static final Ident return_ident         = g("return");
  public static final Ident sizeof_ident         = g("sizeof");
  public static final Ident static_ident         = g("static");
  public static final Ident while_ident          = g("while");
  // literals
  public static final Ident true_ident           = g("true");
  public static final Ident false_ident          = g("false");
  public static final Ident this_ident           = g("this");
  // new syntax:
  public static final Ident cast_ident           = g("cast");
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
  
  //@formatter:on

}
