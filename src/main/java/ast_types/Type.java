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

import ast_class.ClassDeclaration;
import ast_sourceloc.Location;
import ast_sourceloc.SourceLocation;
import errors.ErrorLocation;
import tokenize.Ident;
import tokenize.Token;
import utils_oth.NullChecker;

public class Type implements Serializable, TypeApi, Location {
  private static final long serialVersionUID = -4630043454712001308L;

  /// location, for debug printing
  /// 
  private /*final*/ Token beginPos;

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

  /// only for 'array<T>' and 'string' classes from 'std-package'
  /// it is more clean by design to bootstrap the array only in this
  /// class, with template type-parameters, and
  /// in the future we can easily replace each declaration like 
  /// let arr: [i32] to 'let arr: array<T>'
  /// but this is later: it is syntax sugar only, and means nothing.
  ///
  private TypeBuiltinArray builtinArrayType;

  public void fillPropValues(Type another) {
    NullChecker.check(another);

    this.beginPos = another.beginPos;
    this.size = another.size;
    this.align = another.align;
    this.base = another.base;
    this.classTypeRef = another.classTypeRef;
    this.typenameId = another.typenameId;
    this.builtinArrayType = another.builtinArrayType;
  }

  public Type(TypeBuiltinArray builtinArrayType, Token beginPos) {
    this.base = TypeBase.TP_BUILTIN_ARRAY;
    this.beginPos = beginPos;

    this.builtinArrayType = builtinArrayType;
  }

  public Type(Token beginPos) {
    this.base = TypeBase.TP_void;
    this.beginPos = beginPos;

    this.size = 1;
    this.align = 1;
  }

  public Type(TypeBase primitiveType, Token beginPos) {
    NullChecker.check(primitiveType);

    if (!is_primitive(primitiveType)) {
      ErrorLocation.errorType("expect primitive type", this);
    }
    this.base = primitiveType;
    this.beginPos = beginPos;

    //size
    this.size = TypeBindings.getPrimitiveTypeSize(this);
    this.align = this.size;
  }

  public Type(ClassTypeRef ref, Token beginPos) {
    NullChecker.check(ref);

    this.base = TypeBase.TP_CLASS;
    this.beginPos = beginPos;
    this.classTypeRef = ref;
  }

  public Type(Ident typenameId, Token beginPos) {
    NullChecker.check(typenameId);

    this.base = TP_TYPENAME_ID;
    this.beginPos = beginPos;
    this.typenameId = typenameId;
  }

  public TypeBuiltinArray getBuiltinArrayType() {
    return builtinArrayType;
  }

  public ClassTypeRef getClassTypeRef() {
    return classTypeRef;
  }

  public ClassDeclaration getClassTypeFromRef() {
    if (!is_class()) {
      ErrorLocation.errorType("is not a class", this);
    }
    return classTypeRef.getClazz();
  }

  public List<Type> getTypeArgumentsFromRef() {
    if (!is_class()) {
      ErrorLocation.errorType("is not a class", this);
    }
    return classTypeRef.getTypeArguments();
  }

  public Ident getTypenameId() {
    if (!is_typename_id()) {
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
  public boolean is_equal_to(Type another) {
    NullChecker.check(another);

    // short: two pointers are equal
    if (this == another) {
      return true;
    }

    if (is(TypeBase.TP_char)) {
      if (!another.is(TypeBase.TP_char)) {
        return false;
      }
    }

    else if (is(TypeBase.TP_short)) {
      if (!another.is(TypeBase.TP_short)) {
        return false;
      }
    }

    else if (is(TypeBase.TP_int)) {
      if (!another.is(TypeBase.TP_int)) {
        return false;
      }
    }

    else if (is(TypeBase.TP_long)) {
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
    } else if (is(TypeBase.TP_void)) {
      if (!another.is(TypeBase.TP_void)) {
        return false;
      }
    } else if (is(TypeBase.TP_TYPENAME_ID)) {
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
      if (!classTypeRef.is_equal_to(another.getClassTypeRef())) {
        return false;
      }
    } else if (is(TypeBase.TP_BUILTIN_ARRAY)) {
      if (!another.is(TypeBase.TP_BUILTIN_ARRAY)) {
        return false;
      }
      final TypeBuiltinArray anotherArr = another.getBuiltinArrayType();
      if (!builtinArrayType.getType().is_equal_to(anotherArr.getType())) {
        return false;
      }
    } else {
      ErrorLocation.errorType("unimplemented comparison for base: " + base.toString(), this);
    }

    return true;

  }

  @Override
  public String toString() {
    if (is_primitive()) {
      return TypeBindings.BIND_PRIMITIVE_TO_STRING.get(base);
    }
    if (is_typename_id()) {
      return typenameId.getName();
    }
    if (is_void()) {
      return "void";
    }
    if (is_class()) {
      return classTypeRef.toString();
    }
    if (is_builtin_array()) {
      return builtinArrayType.toString();
    }
    return base.toString();
  }

  public boolean is(TypeBase withBase) {
    return this.base.equals(withBase);
  }

  private boolean isOneOf(TypeBase withBase[]) {
    for (TypeBase item : withBase) {
      if (is(item)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean is_char() {
    return is(TP_char);
  }

  @Override
  public boolean is_short() {
    return is(TP_short);
  }

  @Override
  public boolean is_int() {
    return is(TP_int);
  }

  @Override
  public boolean is_long() {
    return is(TP_long);
  }

  @Override
  public boolean is_float() {
    return is(TP_float);
  }

  @Override
  public boolean is_double() {
    return is(TP_double);
  }

  @Override
  public boolean is_boolean() {
    return is(TP_boolean);
  }

  @Override
  public boolean is_void() {
    return is(TP_void);
  }

  @Override
  public boolean is_primitive() {
    return is_primitive(base);
  }

  @Override
  public boolean is_typename_id() {
    return is(TP_TYPENAME_ID);
  }

  @Override
  public boolean is_class() {
    return is(TP_CLASS);
  }

  @Override
  public boolean is_builtin_array() {
    return is(TypeBase.TP_BUILTIN_ARRAY);
  }

  @Override
  public boolean is_class_template() {
    return is_class() && classTypeRef.isTemplate();
  }

  @Override
  public int get_size() {
    return size;
  }

  @Override
  public int get_align() {
    return align;
  }

  @Override
  public boolean is_reference() {
    return is_class();
  }

  @Override
  public boolean is_numeric() {
    return is_integer() || is_floating();
  }

  @Override
  public boolean is_integer() {
    TypeBase bases[] = { TP_char, TP_short, TP_int, TP_long };
    return isOneOf(bases);
  }

  @Override
  public boolean is_floating() {
    return is(TP_float) || is(TypeBase.TP_double);
  }

  public void set_size(int i) {
    this.size = i;
  }

  @Override
  public SourceLocation getLocation() {
    return beginPos.getLocation();
  }

  @Override
  public String getLocationToString() {
    return beginPos.getLocationToString();
  }

  @Override
  public Token getBeginPos() {
    return beginPos;
  }
}
