package ast_symtab;

import hashed.Hash_ident;
import tokenize.Ident;

public abstract class BuiltinNames {

  // predefined classes
  public static final Ident array_ident = g("array");
  public static final Ident string_ident = g("string");

  // predefined parameters
  public static final Ident __this_ident = g("__this");

  /// means: address of the generated empty const struct
  /// 
  public static final Ident zero_ident = g("zero");
  public static final Ident assert_ident = g("assert");

  // these names are not keywords.
  private static Ident g(String name) {
    return Hash_ident.getHashedIdent(name, 0);
  }

  public static boolean isCorrectBuiltinIdent(Ident id) {
    return id.equals(BuiltinNames.zero_ident);
  }

}
