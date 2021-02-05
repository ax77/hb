package ast_builtins;

import hashed.Hash_ident;
import tokenize.Ident;

public abstract class BuiltinNames {

  // builtin.something
  public static final Ident BUILTIN_IDENT = Hash_ident.getHashedIdent("builtin");

  // arrays
  public static final Ident ARRAY_DECLARE_IDENT = Hash_ident.getHashedIdent("array_declare");
  public static final Ident ARRAY_CLASS_IDENT = Hash_ident.getHashedIdent("array");

  // functions
  public static final Ident BUILTIN_READ_FILE = Hash_ident.getHashedIdent("read_file");
  public static final Ident BUILTIN_WRITE_FILE = Hash_ident.getHashedIdent("write_file");
  public static final Ident BUILTIN_PANIC = Hash_ident.getHashedIdent("panic");

  // iterators
  public static final Ident GET_ITERATOR_METHOD_NAME = Hash_ident.getHashedIdent("get_iterator");
  public static final Ident ITERATOR_HAS_NEXT_METHOD_NAME = Hash_ident.getHashedIdent("has_next");
  public static final Ident ITERATOR_GET_NEXT_METHOD_NAME = Hash_ident.getHashedIdent("get_next");
  
  // strings
  public static final Ident STRING_CLASS_IDENT = Hash_ident.getHashedIdent("string");

}
