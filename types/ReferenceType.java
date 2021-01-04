package njast.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.errors.EParseException;

public class ReferenceType implements Serializable {
  private static final long serialVersionUID = -8581315731092130356L;

  private ReferenceTypeBase base;
  private ClassDeclaration classType;
  private List<ReferenceType> typeArguments;
  private Ident typeVariable;

  public ReferenceType(ClassDeclaration classType) {
    this.base = ReferenceTypeBase.CLASS_REF;
    this.classType = classType;
    this.typeArguments = new ArrayList<ReferenceType>(0);
  }

  public void fillPropValues(ReferenceType other) {
    this.base = other.base;
    this.classType = other.classType;
    this.typeArguments = other.typeArguments;
    this.typeVariable = other.typeVariable;
  }

  public ReferenceType(Ident typeVariable) {
    this.base = ReferenceTypeBase.TYPE_VARIABLE_T;
    this.typeVariable = typeVariable;
    this.typeArguments = new ArrayList<ReferenceType>(0);
  }

  public void setClassType(ClassDeclaration classType) {
    this.classType = classType;
  }

  public Ident getTypeVariable() {
    return typeVariable;
  }

  public void setTypeVariable(Ident typeVariable) {
    this.typeVariable = typeVariable;
  }

  public ReferenceTypeBase getBase() {
    return base;
  }

  public void setTypeArguments(List<ReferenceType> typeArguments) {
    this.typeArguments = typeArguments;
  }

  public void putTypeArgument(ReferenceType e) {
    this.typeArguments.add(e);
  }

  public ClassDeclaration getClassType() {
    return classType;
  }

  public boolean hasTypeArguments() {
    return !typeArguments.isEmpty();
  }

  public boolean isClassTemplate() {
    return base == ReferenceTypeBase.CLASS_REF && classType.isTemplate();
  }

  public List<ReferenceType> getTypeParameters() {
    if (!isClassTemplate()) {
      throw new EParseException("it is not a class template.");
    }
    final List<ReferenceType> typeParameters = classType.getTypeParametersT();
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
    if (base == ReferenceTypeBase.CLASS_REF) {
      sb.append(classType.getIdentifier().getName());
    } else {
      sb.append(typeVariable.getName());
    }

    //    final int bound = typeArguments.size();
    //    if (bound > 0) {
    //      sb.append("_");
    //    }
    //    for (int i = 0; i < bound; i++) {
    //      ReferenceType ref = typeArguments.get(i);
    //      sb.append(ref.toString());
    //      if (i + 1 < bound) {
    //        sb.append("_");
    //      }
    //    }
    //    if (bound > 0) {
    //      //sb.append(">");
    //    }

    return sb.toString();
  }

}
