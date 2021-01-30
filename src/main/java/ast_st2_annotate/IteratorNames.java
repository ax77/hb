package ast_st2_annotate;

import hashed.Hash_ident;
import tokenize.Ident;

public abstract class IteratorNames {

  public static final Ident GET_ITERATOR_METHOD_NAME = Hash_ident.getHashedIdent("get_iterator");
  public static final Ident ITERATOR_HAS_NEXT_METHOD_NAME = Hash_ident.getHashedIdent("has_next");
  public static final Ident ITERATOR_GET_NEXT_METHOD_NAME = Hash_ident.getHashedIdent("get_next");

}
