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

  public boolean isEqualAsGeneric(ReferenceType another) {
    if (this == another) {
      return true;
    }
    if (!base.equals(another.getBase())) {
      return false;
    }

    if (base == ReferenceTypeBase.CLASS_REF) {
      if (!classType.isEqualAsGeneric(another.getClassType())) {
        return false;
      }
      final List<ReferenceType> typeArgumentsAnother = another.getTypeArguments();
      if (typeArguments.size() != typeArgumentsAnother.size()) {
        return false;
      }
      for (int i = 0; i < typeArguments.size(); i++) {
        ReferenceType tp1 = typeArguments.get(i);
        ReferenceType tp2 = typeArgumentsAnother.get(i);
        if (!tp1.isEqualAsGeneric(tp2)) {
          return false;
        }
      }
    } else if (base == ReferenceTypeBase.TYPE_VARIABLE_T) {
      if (!typeVariable.equals(another.getTypeVariable())) {
        return false;
      }
    } else {
      throw new EParseException("unknown base");
    }

    return true;
  }

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

  public Ident getTypeVariable() {
    return typeVariable;
  }

  public ReferenceTypeBase getBase() {
    return base;
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

  public boolean isTypeVarRef() {
    return base == ReferenceTypeBase.TYPE_VARIABLE_T;
  }

  public boolean isClassRef() {
    return base == ReferenceTypeBase.CLASS_REF;
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
    return sb.toString();
  }

}
