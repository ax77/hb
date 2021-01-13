package njast;

import jscan.symtab.Ident;
import njast.types.Type;

public interface IModTypeNameHeader {
  Type getType();

  Ident getIdentifier();

  String getLocationToString();

}
