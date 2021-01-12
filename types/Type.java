package njast.types;

import static njast.types.TypeBase.TP_BOOLEAN;
import static njast.types.TypeBase.TP_BYTE;
import static njast.types.TypeBase.TP_CHAR;
import static njast.types.TypeBase.TP_DOUBLE;
import static njast.types.TypeBase.TP_FLOAT;
import static njast.types.TypeBase.TP_INT;
import static njast.types.TypeBase.TP_LONG;
import static njast.types.TypeBase.TP_SHORT;
import static njast.types.TypeBase.TP_TYPE_VARIABLE_TYPENAME_T;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.errors.EParseException;
import njast.parse.NullChecker;

public class Type implements Serializable {
  private static final long serialVersionUID = -4630043454712001308L;

  private TypeBase base;
  private ClassDeclaration classType;
  private List<Type> typeArguments;
  private Ident typeVariable;

  public final static Type BYTE_TYPE = new Type(TypeBase.TP_BYTE);
  public final static Type SHORT_TYPE = new Type(TypeBase.TP_SHORT);
  public final static Type CHAR_TYPE = new Type(TypeBase.TP_CHAR);
  public final static Type INT_TYPE = new Type(TypeBase.TP_INT);
  public final static Type LONG_TYPE = new Type(TypeBase.TP_LONG);
  public final static Type FLOAT_TYPE = new Type(TypeBase.TP_FLOAT);
  public final static Type DOUBLE_TYPE = new Type(TypeBase.TP_DOUBLE);
  public final static Type BOOLEAN_TYPE = new Type(TypeBase.TP_BOOLEAN);

  public void fillPropValues(Type another) {
    NullChecker.check(another);

    this.base = another.base;
    this.classType = another.classType;
    this.typeArguments = another.typeArguments;
    this.typeVariable = another.typeVariable;
  }

  public Type() {
    this.base = TypeBase.TP_VOID_STUB;
  }

  public Type(TypeBase primitiveType) {
    NullChecker.check(primitiveType);

    if (!isPrimitive(primitiveType)) {
      throw new EParseException("expect primitive type");
    }
    this.base = primitiveType;
    this.typeArguments = new ArrayList<Type>(0);
  }

  public Type(ClassDeclaration classType) {
    NullChecker.check(classType);

    this.base = TypeBase.TP_CLASS;
    this.classType = classType;
    this.typeArguments = new ArrayList<Type>(0);
  }

  public Type(Ident typeVariable) {
    NullChecker.check(typeVariable);

    this.base = TP_TYPE_VARIABLE_TYPENAME_T;
    this.typeVariable = typeVariable;
    this.typeArguments = new ArrayList<Type>(0);
  }

  public boolean isClassTemplate() {
    return isClassRef() && classType.isTemplate();
  }

  public void putTypeArgument(Type e) {
    NullChecker.check(e);

    this.typeArguments.add(e);
  }

  public ClassDeclaration getClassType() {
    if (!isClassRef()) {
      throw new EParseException("is not a class");
    }
    return classType;
  }

  public List<Type> getTypeArguments() {
    return typeArguments;
  }

  public Ident getTypeVariable() {
    return typeVariable;
  }

  public TypeBase getBase() {
    return base;
  }

  public boolean isTypeVarRef() {
    return base == TypeBase.TP_TYPE_VARIABLE_TYPENAME_T;
  }

  public boolean isClassRef() {
    return base == TypeBase.TP_CLASS;
  }

  public Ident getTypeParameter() {
    if (!isTypeVarRef()) {
      throw new EParseException("is not typename T");
    }
    return typeVariable;
  }

  public boolean isPrimitive(TypeBase withBase) {
    return withBase == TP_BYTE || withBase == TP_SHORT || withBase == TP_CHAR || withBase == TP_INT
        || withBase == TP_LONG || withBase == TP_FLOAT || withBase == TP_DOUBLE || withBase == TP_BOOLEAN;
  }

  public boolean isPrimitive() {
    return isPrimitive(base);
  }

  public boolean isEqualTo(Type another) {
    NullChecker.check(another);

    if (this == another) {
      return true;
    }

    if (isPrimitive()) {
      if (!base.equals(another.getBase())) {
        return false;
      }
    }

    else if (isClassRef()) {
      final Ident name1 = classType.getIdentifier();
      final Ident name2 = another.getClassType().getIdentifier();
      if (!name1.equals(name2)) {
        return false;
      }
    }

    else if (isVoidStub()) {
      if (!another.isVoidStub()) {
        return false;
      }
    }

    else {
      throw new EParseException("unimpl...");
    }

    return true;
  }

  public boolean isVoidStub() {
    return base == TypeBase.TP_VOID_STUB;
  }

  @Override
  public String toString() {
    if (isPrimitive()) {
      return TypeBindings.BIND_PRIMITIVE_TO_STRING.get(base);
    }
    if (base == TP_TYPE_VARIABLE_TYPENAME_T) {
      return typeVariable.getName();
    }
    if (base == TypeBase.TP_VOID_STUB) {
      return "void";
    }
    return classType.getIdentifier().getName();
  }

}
