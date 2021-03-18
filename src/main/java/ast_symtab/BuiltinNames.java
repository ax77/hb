package ast_symtab;

import hashed.Hash_ident;
import tokenize.Ident;

public abstract class BuiltinNames {

  // builtin.something
  public static final Ident std_ident = g("std");

  // predefined classes
  public static final Ident array_ident = g("array");
  public static final Ident string_ident = g("string");

  // predefined parameters
  public static final Ident __this_ident = g("__this");

  // IO
  public static final Ident read_file_ident = g("read_file");
  public static final Ident print_ident = g("print");
  public static final Ident assert_true_ident = g("assert_true");
  public static final Ident assert_ident = g("assert");

  /// mem
  ///
  /// std.mem_malloc<T>(size);           raw_data = malloc(size)
  /// std.mem_free<T>(raw_data);         free(raw_data)
  /// std.mem_get<T>(raw_data, at);      return raw_data[at]
  /// std.mem_set<T>(raw_data, at, e);   raw_data[at] = e
  public static final Ident mem_malloc_ident = g("mem_malloc");
  public static final Ident mem_free_ident = g("mem_free");
  public static final Ident mem_get_ident = g("mem_get");
  public static final Ident mem_set_ident = g("mem_set");

  // these names are not keywords.
  private static Ident g(String name) {
    return Hash_ident.getHashedIdent(name, 0);
  }

  public static boolean isBuiltinMemFuncIdent(Ident id) {
    return id.equals(BuiltinNames.mem_malloc_ident) || id.equals(BuiltinNames.mem_get_ident)
        || id.equals(BuiltinNames.mem_free_ident) || id.equals(BuiltinNames.mem_set_ident);
  }

  public static boolean isCorrectBuiltinIdent(Ident id) {
    return id.equals(BuiltinNames.read_file_ident) || id.equals(BuiltinNames.print_ident)
        || id.equals(BuiltinNames.assert_true_ident) || isBuiltinMemFuncIdent(id);
  }

}
