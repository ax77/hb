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
    return "REF: " + classType.toString();
  }

}
