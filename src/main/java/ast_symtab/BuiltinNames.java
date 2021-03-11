package ast_symtab;

import hashed.Hash_ident;
import tokenize.Ident;

public abstract class BuiltinNames {

  // builtin.something
  public static final Ident std_ident = g("std");

  // strings
  public static final Ident String_ident = g("String");

  // predefined methods
  public static final Ident opAssign_ident = g("opAssign");

  // predefined parameters
  public static final Ident __this_ident = g("__this");

  // these names are not keywords.
  private static Ident g(String name) {
    return Hash_ident.getHashedIdent(name, 0);
  }

  public static boolean isCorrectBuiltinIdent(Ident id) {
    return id.equals(BuiltinNames.String_ident);
  }

}
