package njast;

import java.util.List;

import njast.types.ReferenceType;

public class Dto {
  private final List<ReferenceType> typeArguments;
  private final ReferenceType result;

  public Dto(List<ReferenceType> typeArguments, ReferenceType result) {
    this.typeArguments = typeArguments;
    this.result = result;
  }

  public List<ReferenceType> getTypeArguments() {
    return typeArguments;
  }

  public ReferenceType getResult() {
    return result;
  }

}
