package ast_symtab;

import hashed.Hash_ident;
import tokenize.Ident;

public abstract class BuiltinNames {

  // builtin.something
  public static final Ident std_ident = g("std");

  // predefined classes
  public static final Ident array_ident = g("array");

  // predefined methods
  public static final Ident opAssign_ident = g("opAssign");

  // predefined parameters
  public static final Ident __this_ident = g("__this");

  // IO
  public static final Ident read_file_ident = g("read_file");
  public static final Ident print_ident = g("print");

  // these names are not keywords.
  private static Ident g(String name) {
    return Hash_ident.getHashedIdent(name, 0);
  }

  public static boolean isCorrectBuiltinIdent(Ident id) {
    return id.equals(BuiltinNames.read_file_ident) || id.equals(BuiltinNames.print_ident);
  }

}
