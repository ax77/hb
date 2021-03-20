package ast_symtab;

import hashed.Hash_ident;
import tokenize.Ident;

public abstract class BuiltinNames {

  // predefined classes
  public static final Ident array_ident = g("array");
  public static final Ident string_ident = g("string");

  // predefined parameters
  public static final Ident __this_ident = g("__this");

  public static final Ident assert_ident = g("assert");
  public static final Ident box_ident = g("box");

  /// natives:
  public static final Ident native_malloc_ident = g("native_malloc");
  public static final Ident native_calloc_ident = g("native_calloc");
  public static final Ident native_free_ident = g("native_free");
  public static final Ident native_ptr_access_at_ident = g("native_ptr_access_at");
  public static final Ident native_ptr_set_at_ident = g("native_ptr_set_at");
  public static final Ident native_memcpy_ident = g("native_memcpy");
  public static final Ident native_panic_ident = g("native_panic");
  public static final Ident native_assert_true_ident = g("native_assert_true");
  public static final Ident native_open_ident = g("native_open");
  public static final Ident native_close_ident = g("native_close");
  public static final Ident native_read_ident = g("native_read");
  public static final Ident native_printf_ident = g("native_printf");

  // these names are not keywords.
  private static Ident g(String name) {
    return Hash_ident.getHashedIdent(name, 0);
  }

  //@formatter:off
  public static boolean isMemClassNativeMethodName(Ident id) {
    return 
            id.equals(native_malloc_ident)
        ||  id.equals(native_calloc_ident)
        ||  id.equals(native_free_ident)
        ||  id.equals(native_ptr_access_at_ident)
        ||  id.equals(native_ptr_set_at_ident)
        ||  id.equals(native_memcpy_ident)
        ;
  }

}
