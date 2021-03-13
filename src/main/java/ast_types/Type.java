package ast_types;

import static ast_types.TypeBase.TP_CLASS;
import static ast_types.TypeBase.TP_TYPENAME_ID;
import static ast_types.TypeBase.TP_boolean;
import static ast_types.TypeBase.TP_char;
import static ast_types.TypeBase.TP_double;
import static ast_types.TypeBase.TP_float;
import static ast_types.TypeBase.TP_int;
import static ast_types.TypeBase.TP_long;
import static ast_types.TypeBase.TP_short;
import static ast_types.TypeBase.TP_void;

import java.io.Serializable;
import java.util.List;

import _st3_linearize_expr.CEscaper;
import ast_class.ClassDeclaration;
import errors.ErrorLocation;
import tokenize.Ident;
import tokenize.Token;
import utils_oth.NullChecker;

public class Type implements Serializable, TypeApi {
  private static final long serialVersionUID = -4630043454712001308L;

  /// main properties
  private int size;
  private int align;
  private TypeBase base;

  /// instantiated class: [new list<i32>();]
  /// where 'list' is a class with its type-parameters: class list<T> {  }
  /// and type-arguments are 'real' given types we have to expand in templates
  ///
  private ClassTypeRef classTypeRef;

  /// class list<T> {  } -> here 'T' is a 'typename T'
  ///
  private Ident typenameId;

  /// private String bytesStub;

  public void fillPropValues(Type another) {
    NullChecker.check(another);

    this.size = another.size;
    this.align = another.align;
    this.base = another.base;
    this.classTypeRef = another.classTypeRef;
    this.typenameId = another.typenameId;
  }

  public Type(Token beginPos) {
    this.base = TypeBase.TP_void;

    this.size = 1;
    this.align = 1;
  }

  public Type(TypeBase primitiveType) {
    NullChecker.check(primitiveType);

    if (!is_primitive(primitiveType)) {
      ErrorLocation.errorType("expect primitive type", this);
    }
    this.base = primitiveType;

    //size
    this.size = TypeBindings.getPrimitiveTypeSize(this);
    this.align = this.size;
  }

  public Type(ClassTypeRef ref) {
    NullChecker.check(ref);

    this.base = TypeBase.TP_CLASS;

    this.classTypeRef = ref;
  }

  public Type(Ident typenameId) {
    NullChecker.check(typenameId);

    this.base = TP_TYPENAME_ID;

    this.typenameId = typenameId;
  }

  /// public Type(String bytesStub) {
  ///   int[] esc = CEscaper.escape(bytesStub);
  ///   this.base = TypeBase.TP_BYTES;
  ///   this.bytesStub = bytesStub;
  ///   this.size = esc.length;
  ///   this.align = 1;
  /// }

  public ClassTypeRef getClassTypeRef() {
    return classTypeRef;
  }

  public ClassDeclaration getClassTypeFromRef() {
    if (!isClass()) {
      ErrorLocation.errorType("is not a class", this);
    }
    return classTypeRef.getClazz();
  }

  public List<Type> getTypeArgumentsFromRef() {
    if (!isClass()) {
      ErrorLocation.errorType("is not a class", this);
    }
    return classTypeRef.getTypeArguments();
  }

  public Ident getTypenameId() {
    if (!isTypenameID()) {
      ErrorLocation.errorType("is not typename T", this);
    }
    return typenameId;
  }

  public TypeBase getBase() {
    return base;
  }

  public boolean is_primitive(TypeBase withBase) {
    //@formatter:off
    return withBase == TP_char
        || withBase == TP_short
        || withBase == TP_int
        || withBase == TP_long
        || withBase == TP_float
        || withBase == TP_double
        || withBase == TP_boolean;
    //@formatter:on
  }

  @Override
  public boolean isEqualTo(Type another) {
    NullChecker.check(another);

    // short: two pointers are equal
    if (this == another) {
      return true;
    }

    if (is(TypeBase.TP_char)) {
      if (!another.is(TypeBase.TP_char)) {
        return false;
      }
    } else if (is(TypeBase.TP_short)) {
      if (!another.is(TypeBase.TP_short)) {
        return false;
      }
    } else if (is(TypeBase.TP_int)) {
      if (!another.is(TypeBase.TP_int)) {
        return false;
      }
    } else if (is(TypeBase.TP_long)) {
      if (!another.is(TypeBase.TP_long)) {
        return false;
      }
    } else if (is(TypeBase.TP_float)) {
      if (!another.is(TypeBase.TP_float)) {
        return false;
      }
    } else if (is(TypeBase.TP_double)) {
      if (!another.is(TypeBase.TP_double)) {
        return false;
      }
    } else if (is(TypeBase.TP_boolean)) {
      if (!another.is(TypeBase.TP_boolean)) {
        return false;
      }
    }

    else if (is(TypeBase.TP_void)) {
      if (!another.is(TypeBase.TP_void)) {
        return false;
      }
    }

    /// else if (is(TypeBase.TP_BYTES)) {
    ///   if (!another.is(TypeBase.TP_BYTES)) {
    ///     return false;
    ///   }
    /// }

    else if (is(TypeBase.TP_TYPENAME_ID)) {
      if (!another.is(TypeBase.TP_TYPENAME_ID)) {
        return false;
      }
      final Ident anotherTypeVariable = another.getTypenameId();
      final String name1 = typenameId.getName();
      final String name2 = anotherTypeVariable.getName();
      if (!name1.equals(name2)) {
        return false;
      }
    } else if (is(TypeBase.TP_CLASS)) {
      if (!another.is(TypeBase.TP_CLASS)) {
        return false;
      }
      if (!classTypeRef.isEqualTo(another.getClassTypeRef())) {
        return false;
      }
    } else {
      ErrorLocation.errorType("unimplemented comparison for base: " + base.toString(), this);
    }

    return true;

  }

  @Override
  public String toString() {
    if (isPrimitive()) {
      return TypeBindings.BIND_PRIMITIVE_TO_STRING.get(base);
    }
    if (isTypenameID()) {
      return typenameId.getName();
    }
    if (isVoid()) {
      return "void";
    }
    if (isClass()) {
      return classTypeRef.toString();
    }
    /// if (isBytes()) {
    ///   return "char*";
    /// }
    return base.toString();
  }

  public boolean is(TypeBase withBase) {
    return this.base.equals(withBase);
  }

  @Override
  public boolean isChar() {
    return is(TP_char);
  }

  @Override
  public boolean isShort() {
    return is(TP_short);
  }

  @Override
  public boolean isInt() {
    return is(TP_int);
  }

  @Override
  public boolean isLong() {
    return is(TP_long);
  }

  @Override
  public boolean isFloat() {
    return is(TP_float);
  }

  @Override
  public boolean isDouble() {
    return is(TP_double);
  }

  @Override
  public boolean isBoolean() {
    return is(TP_boolean);
  }

  @Override
  public boolean isVoid() {
    return is(TP_void);
  }

  @Override
  public boolean isPrimitive() {
    return is_primitive(base);
  }

  @Override
  public boolean isTypenameID() {
    return is(TP_TYPENAME_ID);
  }

  @Override
  public boolean isClass() {
    return is(TP_CLASS);
  }

  @Override
  public boolean isClassTemplate() {
    return isClass() && classTypeRef.isTemplate();
  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public int getAlign() {
    return align;
  }

  @Override
  public boolean isNumeric() {
    return isInteger() || isFloating();
  }

  @Override
  public boolean isInteger() {
    return is(TP_char) || is(TP_short) || is(TP_int) || is(TP_long);
  }

  @Override
  public boolean isFloating() {
    return is(TP_float) || is(TypeBase.TP_double);
  }

  public void set_size(int i) {
    this.size = i;
  }

  /// @Override
  /// public boolean isBytes() {
  ///   return is(TypeBase.TP_BYTES);
  /// }

}
