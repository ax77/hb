package njast.types;

import java.util.ArrayList;
import java.util.List;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.errors.EParseException;

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

  public boolean isClassTemplate() {
    return classType.isTemplate();
  }

  public List<Ident> getTypeParameters() {
    if (!isClassTemplate()) {
      throw new EParseException("it is not a class template.");
    }
    final List<Ident> typeParameters = classType.getTypeParameters().getTypeParameters();
    if (!hasTypeArguments()) {
      throw new EParseException("template class without arguments");
    }
    if (typeParameters.size() != typeArguments.size()) {
      throw new EParseException("count of parameters and arguments should be equal.");
    }
    return typeParameters;
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
      sb.append("_");
    }
    for (int i = 0; i < bound; i++) {
      ReferenceType ref = typeArguments.get(i);
      sb.append(ref.toString());
      if (i + 1 < bound) {
        sb.append("_");
      }
    }
    if (bound > 0) {
      //sb.append(">");
    }

    return sb.toString();
  }

}
