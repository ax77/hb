package njast;

import java.util.List;

import njast.types.Type;

public class Dto {
  private final List<Type> typeArguments;
  private final Type result;

  public Dto(List<Type> typeArguments, Type result) {
    this.typeArguments = typeArguments;
    this.result = result;
  }

  public List<Type> getTypeArguments() {
    return typeArguments;
  }

  public Type getResult() {
    return result;
  }

}
