package njast.types;

import java.util.ArrayList;
import java.util.List;

import njast.ast_nodes.clazz.ClassDeclaration;

public class ReferenceType {
  private final ClassDeclaration classType;
  private List<ReferenceType> typeArguments;

  public ReferenceType(ClassDeclaration classType) {
    this.classType = classType;
    this.typeArguments = new ArrayList<ReferenceType>(0);
  }

  public void putTypeArgument(ReferenceType e) {
    this.typeArguments.add(e);
  }

  public ClassDeclaration getTypeName() {
    return classType;
  }

  public boolean hasTypeArguments() {
    return !typeArguments.isEmpty();
  }

  public List<ReferenceType> getTypeArguments() {
    return typeArguments;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(classType.getIdentifier().getName());

    final int bound = typeArguments.size();
    if (bound > 0) {
      sb.append("<");
    }
    for (int i = 0; i < bound; i++) {
      ReferenceType ref = typeArguments.get(i);
      sb.append(ref.toString());
      if (i + 1 < bound) {
        sb.append(", ");
      }
    }
    if (bound > 0) {
      sb.append(">");
    }

    return sb.toString();
  }

}
