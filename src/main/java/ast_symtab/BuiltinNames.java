package ast_symtab;

import hashed.Hash_ident;
import tokenize.Ident;

public abstract class BuiltinNames {

  // predefined classes
  public static final Ident array_ident = g("array");
  public static final Ident string_ident = g("string");
  public static final Ident opt_ident = g("opt");

  // predefined parameters
  public static final Ident __this_ident = g("__this");

  public static final Ident native_open_ident = g("native_open");
  public static final Ident native_close_ident = g("native_close");
  public static final Ident native_read_ident = g("native_read");
  public static final Ident print_ident = g("print");

  // these names are not keywords.
  private static Ident g(String name) {
    return Hash_ident.getHashedIdent(name, 0);
  }

}
