package njast.templates;

import java.util.List;

import jscan.symtab.Ident;
import njast.ast.nodes.ClassDeclaration;
import njast.parse.AstParseException;
import njast.parse.NullChecker;
import njast.types.ArrayType;
import njast.types.Type;
import njast.types.TypeBase;

public abstract class TypeComparerForTemplates {

  public static boolean typeArgumentListsAreEqualForTemplate(List<Type> first, List<Type> second) {

    NullChecker.check(first, second);

    if (first.size() != second.size()) {
      return false;
    }

    for (int i = 0; i < first.size(); i++) {
      Type tp1 = first.get(i);
      Type tp2 = second.get(i);
      if (!typesAreEqualAsGenerics(tp1, tp2)) {
        return false;
      }
    }

    return true;
  }

  private static boolean classesAreEqualAsGenerics(ClassDeclaration first, ClassDeclaration another) {
    NullChecker.check(first, another);

    if (first == another) {
      return true;
    }

    final Ident id1 = first.getIdentifier();
    final Ident id2 = another.getIdentifier();
    if (!id1.equals(id2)) {
      return false;
    }

    final List<Type> args1 = first.getTypeParametersT();
    final List<Type> args2 = another.getTypeParametersT();
    if (!typeArgumentListsAreEqualForTemplate(args1, args2)) {
      return false;
    }

    return true;
  }

  private static boolean typesAreEqualAsGenerics(Type first, Type another) {
    NullChecker.check(first, another);

    if (first == another) {
      return true;
    }

    final TypeBase base1 = first.getBase();
    final TypeBase base2 = another.getBase();

    // for speed only.
    // we'll check through all bases although.
    if (!base1.equals(base2)) {
      return false;
    }

    // through all bases...
    // because if we'll add new base - and forget to compare it
    // it'll be better the error instead of silence.
    //
    if (first.isPrimitive()) {
      if (!base1.equals(base2)) {
        return false;
      }
    }

    else if (base1 == TypeBase.TP_CLASS) {
      final ClassDeclaration clazz1 = first.getClassType();
      final ClassDeclaration clazz2 = another.getClassType();
      if (!classesAreEqualAsGenerics(clazz1, clazz2)) {
        return false;
      }
      final List<Type> args1 = first.getTypeArguments();
      final List<Type> args2 = another.getTypeArguments();
      if (!typeArgumentListsAreEqualForTemplate(args1, args2)) {
        return false;
      }
    }

    else if (base1 == TypeBase.TP_TYPE_VARIABLE_TYPENAME_T) {
      final Ident typename1 = first.getTypeVariable();
      final Ident typename2 = another.getTypeVariable();
      if (!typename1.equals(typename2)) {
        return false;
      }
    }

    else if (base1 == TypeBase.TP_ARRAY) {
      if (!another.isArray()) {
        return false;
      }

      final ArrayType array1 = first.getArrayType();
      final ArrayType array2 = another.getArrayType();

      if (array1.getCount() != array2.getCount()) {
        return false;
      }
      final Type sub1 = array1.getArrayOf();
      final Type sub2 = array2.getArrayOf();
      if (!typesAreEqualAsGenerics(sub1, sub2)) {
        return false;
      }
    }

    else {
      throw new AstParseException("unknown base");
    }

    return true;
  }
}
