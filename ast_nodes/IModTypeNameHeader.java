package njast.ast_nodes;

import jscan.symtab.Ident;
import njast.types.Type;

public interface IModTypeNameHeader {
  Type getType();

  Ident getIdentifier();

}
