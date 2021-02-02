package ast_types;

import tokenize.Ident;
import tokenize.Token;

public class TypeUnresolvedId {
  private final Ident typeName; 
  private final Token beginPos;

  public TypeUnresolvedId(Ident typeName, Token beginPos) {
    this.typeName = typeName;
    this.beginPos = beginPos;
  }

  public Ident getTypeName() {
    return typeName;
  }

  public Token getBeginPos() {
    return beginPos;
  }

  @Override
  public String toString() {
    return typeName.toString();
  }

}
