package ast_types;

import java.io.Serializable;
import java.util.List;

import tokenize.Ident;
import tokenize.Token;

public class TypeUnresolvedId implements Serializable {
  private static final long serialVersionUID = 8959530510216631676L;

  private final Ident typeName;
  private final Token beginPos;
  private final List<Type> typeArguments;

  public TypeUnresolvedId(Ident typeName, List<Type> typeArguments, Token beginPos) {
    this.typeName = typeName;
    this.typeArguments = typeArguments;
    this.beginPos = beginPos;
  }

  public List<Type> getTypeArguments() {
    return typeArguments;
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
