package ast_builtins;

import hashed.Hash_ident;
import tokenize.Ident;

public abstract class BuiltinNames {

  //@formatter:off

  // builtin.something
  public static final Ident builtin_ident        = g("builtin");
  // arrays
  public static final Ident array_ident          = g("ArrayList");
  public static final Ident array_declare_ident  = g("array_declare");
  public static final Ident array_add_ident      = g("array_add");
  public static final Ident array_size_ident     = g("array_size");
  public static final Ident array_get_ident      = g("array_get");
  public static final Ident array_set_ident      = g("array_set");
  public static final Ident array_insert_ident   = g("array_insert");
  // functions
  public static final Ident read_file_ident      = g("read_file");
  public static final Ident write_file_ident     = g("write_file");
  public static final Ident panic_ident          = g("panic");
  // iterators
  public static final Ident get_iterator_ident   = g("get_iterator");
  public static final Ident has_next_ident       = g("has_next");
  public static final Ident get_next_ident       = g("get_next");
  // strings
  public static final Ident string_ident         = g("String");

  // these names are not keywords.
  private static Ident g(String name) {
    return Hash_ident.getHashedIdent(name, 0);
  }
  
  public static boolean isCorrectBuiltinIdent(Ident id) {
    return
       id.equals(BuiltinNames.builtin_ident       )
    || id.equals(BuiltinNames.array_ident         )
    || id.equals(BuiltinNames.array_declare_ident )
    || id.equals(BuiltinNames.array_add_ident     )
    || id.equals(BuiltinNames.array_size_ident    )
    || id.equals(BuiltinNames.array_get_ident     )
    || id.equals(BuiltinNames.array_set_ident     )
//    || id.equals(BuiltinNames.array_insert_ident  )
    || id.equals(BuiltinNames.read_file_ident     )
    || id.equals(BuiltinNames.write_file_ident    )
    || id.equals(BuiltinNames.panic_ident         )
    || id.equals(BuiltinNames.get_iterator_ident  )
    || id.equals(BuiltinNames.has_next_ident      )
    || id.equals(BuiltinNames.get_next_ident      )
    || id.equals(BuiltinNames.string_ident        )
    ;
  }

}
