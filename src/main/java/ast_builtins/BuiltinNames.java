package ast_builtins;

import hashed.Hash_ident;
import tokenize.Ident;

public abstract class BuiltinNames {

  //@formatter:off

  // builtin.something
  public static final Ident std_ident            = g("std");
  // arrays
  public static final Ident ArrayList_ident      = g("ArrayList");
  public static final Ident array_declare_ident  = g("array_declare");
  public static final Ident array_add_ident      = g("array_add");
  public static final Ident array_size_ident     = g("array_size");
  public static final Ident array_get_ident      = g("array_get");
  public static final Ident array_set_ident      = g("array_set");
  public static final Ident array_allocate_ident = g("array_allocate");
  // iterators
  public static final Ident get_iterator_ident   = g("get_iterator");
  public static final Ident has_next_ident       = g("has_next");
  public static final Ident get_next_ident       = g("get_next");
  // strings
  public static final Ident String_ident         = g("String");
  // predefined methods
  public static final Ident opAssign_ident       = g("opAssign");
  //
  public static final Ident __this_ident         = g("__this");

  // these names are not keywords.
  private static Ident g(String name) {
    return Hash_ident.getHashedIdent(name, 0);
  }
  
  public static boolean isCorrectBuiltinIdent(Ident id) {
    return 
       id.equals(BuiltinNames.ArrayList_ident           )
    || id.equals(BuiltinNames.array_declare_ident   )
    || id.equals(BuiltinNames.array_add_ident       )
    || id.equals(BuiltinNames.array_size_ident      )
    || id.equals(BuiltinNames.array_get_ident       )
    || id.equals(BuiltinNames.array_set_ident       )
    || id.equals(BuiltinNames.array_allocate_ident  )
    //
    || id.equals(BuiltinNames.get_iterator_ident    )
    || id.equals(BuiltinNames.has_next_ident        )
    || id.equals(BuiltinNames.get_next_ident        )
    //
    || id.equals(BuiltinNames.String_ident          )
    ;
  }

}
