package njast.ast.mir;

import jscan.symtab.Ident;
import njast.symtab.IdentMap;

public abstract class Renamer {

  //for( type item = iter.current(); iter.has_next(); item = iter.get_next() ) {}
  //
  public static final Ident GET_ITERATOR_METHOD_NAME = IdentMap.get_iterator_ident;
  public static final Ident ITERATOR_GET_CURRENT_METHOD_NAME = IdentMap.get_current_ident;
  public static final Ident ITERATOR_HAS_NEXT_METHOD_NAME = IdentMap.has_next_ident;
  public static final Ident ITERATOR_GET_NEXT_METHOD_NAME = IdentMap.get_next_ident;

}
