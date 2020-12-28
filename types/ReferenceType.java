package njast.types;

import jscan.symtab.Ident;

public class ReferenceType {
  private final Ident typeName;

  public ReferenceType(Ident typeName) {
    this.typeName = typeName;
  }

  public Ident getTypeName() {
    return typeName;
  }

}
