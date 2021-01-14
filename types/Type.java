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
import java.util.List;

import jscan.symtab.Ident;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.errors.EParseException;
import njast.parse.NullChecker;

public class Type implements Serializable {
  private static final long serialVersionUID = -4630043454712001308L;

  private TypeBase base;
  private Ref ref;
  private Ident typeVariable;
  private ArrayType array;

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
    this.ref = another.ref;
    this.typeVariable = another.typeVariable;
  }

  public Type() {
    this.base = TypeBase.TP_VOID_STUB;
  }

  public Type(ArrayType array) {
    this.base = TypeBase.TP_ARRAY;
    this.array = array;
  }

  public Type(TypeBase primitiveType) {
    NullChecker.check(primitiveType);

    if (!isPrimitive(primitiveType)) {
      throw new EParseException("expect primitive type");
    }
    this.base = primitiveType;
  }

  public Type(Ref ref) {
    NullChecker.check(ref);

    this.base = TypeBase.TP_CLASS;
    this.ref = ref;
  }

  public Type(Ident typeVariable) {
    NullChecker.check(typeVariable);

    this.base = TP_TYPE_VARIABLE_TYPENAME_T;
    this.typeVariable = typeVariable;
  }

  public boolean isClassTemplate() {
    return isClassRef() && ref.isTemplate();
  }

  public ArrayType getArray() {
    return array;
  }

  public ClassDeclaration getClassType() {
    if (!isClassRef()) {
      throw new EParseException("is not a class");
    }
    return ref.getClazz();
  }

  public List<Type> getTypeArguments() {
    if (!isClassRef()) {
      throw new EParseException("is not a class");
    }
    return ref.getTypeArguments();
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
      final Ident name1 = ref.getClazz().getIdentifier();
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

    else if (isArray()) {
      if (!another.isArray()) {
        return false;
      }
      final ArrayType anotherArray = another.getArray();
      if (array.getCount() != anotherArray.getCount()) {
        return false;
      }
      final Type sub1 = array.getArrayOf();
      final Type sub2 = anotherArray.getArrayOf();
      if (!sub1.isEqualTo(sub2)) {
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
    if (isTypeVarRef()) {
      return typeVariable.getName();
    }
    if (isVoidStub()) {
      return "void";
    }
    if (isArray()) {
      return array.toString();
    }
    return ref.toString();
  }

  public boolean isArray() {
    return base == TypeBase.TP_ARRAY;
  }

}
