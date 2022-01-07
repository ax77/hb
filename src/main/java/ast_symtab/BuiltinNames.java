package ast_symtab;

import hashed.Hash_ident;
import tokenize.Ident;

public abstract class BuiltinNames {

  // predefined classes
  public static final Ident arr_ident = g("arr");
  public static final Ident str_ident = g("str");

  // predefined parameters
  public static final Ident __this_ident = g("__this");

  public static final Ident native_open_ident = g("open");
  public static final Ident native_close_ident = g("close");
  public static final Ident native_read_ident = g("read");
  public static final Ident native_print_ident = g("print");
  public static final Ident native_hashcode_ident = g("hashcode");

  public static final Ident equals_ident = g("equals");

  // these names are not keywords.
  private static Ident g(String name) {
    return Hash_ident.getHashedIdent(name, 0);
  }

}
