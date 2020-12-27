package njast.symtab;

import jscan.hashed.Hash_ident;
import jscan.symtab.Ident;

public final class IdentMap {

  public static final int ___ = 0;
  public static final int C89 = 1 << 0;
  public static final int C99 = 1 << 1;
  public static final int C11 = 1 << 2;
  public static final int C2X = 1 << 3;
  public static final int NS_GNU = 1 << 4;
  public static final int NS_MSV = 1 << 5;
  public static final int NS_RID = 1 << 6;
  public static final int NS_ALL = C89 | C99 | C11;

  // <class modifier>           ::= public | abstract | final
  // <field modifier>           ::= public | protected | private | static | final | transient | volatile
  // <method modifier>          ::= public | protected | private | static | abstract | final | synchronized | native
  // <constructor modifier>     ::= public | protected | private
  // <interface modifier>       ::= public | abstract
  // <abstract method modifier> ::= public | abstract
  //
  // <keyword> ::= abstract | boolean | break | byte | case | catch | char | class | const | continue | default | do | double | else | extends | final | finally | float | for | goto | if | implements | import | instanceof | int | interface | long | native | new | package | private | protected | public | return | short | static | super | switch | synchronized | this | throw | throws | transient | try | void | volatile | while

  //@formatter:off
  public static final Ident abstract_ident            = g("abstract");
  public static final Ident boolean_ident             = g("boolean");
  public static final Ident break_ident               = g("break");
  public static final Ident byte_ident                = g("byte");
  public static final Ident case_ident                = g("case");
  public static final Ident catch_ident               = g("catch");
  public static final Ident char_ident                = g("char");
  public static final Ident class_ident               = g("class");
  public static final Ident const_ident               = g("const");
  public static final Ident continue_ident            = g("continue");
  public static final Ident default_ident             = g("default");
  public static final Ident do_ident                  = g("do");
  public static final Ident double_ident              = g("double");
  public static final Ident else_ident                = g("else");
  public static final Ident enum_ident                = g("enum");
  public static final Ident extends_ident             = g("extends");
  public static final Ident final_ident               = g("final");
  public static final Ident finally_ident             = g("finally");
  public static final Ident float_ident               = g("float");
  public static final Ident for_ident                 = g("for");
  public static final Ident goto_ident                = g("goto");
  public static final Ident if_ident                  = g("if");
  public static final Ident implements_ident          = g("implements");
  public static final Ident import_ident              = g("import");
  public static final Ident instanceof_ident          = g("instanceof");
  public static final Ident int_ident                 = g("int");
  public static final Ident interface_ident           = g("interface");
  public static final Ident long_ident                = g("long");
  public static final Ident native_ident              = g("native");
  public static final Ident new_ident                 = g("new");
  public static final Ident package_ident             = g("package");
  public static final Ident private_ident             = g("private");
  public static final Ident protected_ident           = g("protected");
  public static final Ident public_ident              = g("public");
  public static final Ident return_ident              = g("return");
  public static final Ident short_ident               = g("short");
  public static final Ident static_ident              = g("static");
  public static final Ident super_ident               = g("super");
  public static final Ident switch_ident              = g("switch");
  public static final Ident synchronized_ident        = g("synchronized");
  public static final Ident this_ident                = g("this");
  public static final Ident throw_ident               = g("throw");
  public static final Ident throws_ident              = g("throws");
  public static final Ident transient_ident           = g("transient");
  public static final Ident try_ident                 = g("try");
  public static final Ident void_ident                = g("void");
  public static final Ident volatile_ident            = g("volatile");
  public static final Ident while_ident               = g("while");
  //@formatter:on

  private static Ident g(String name) {
    return Hash_ident.getHashedIdent(name, 32);
  }

}
