package ast_st2_annotate;

import java.util.ArrayList;

import ast_builtins.BuiltinNames;
import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_method.ClassMethodDeclaration;
import ast_types.Type;
import ast_types.TypeBase;
import errors.AstParseException;

public class IteratorChecker {

  private final Type typeGiven;
  private final boolean isIterable;
  private Type elemType;
  private Type iteratorType;

  public IteratorChecker(final Type type) {
    this.typeGiven = type;
    this.isIterable = checkIsIterable(type);
  }

  public boolean isIterable() {
    return isIterable;
  }

  public Type getElemType() {
    if (elemType == null) {
      throw new AstParseException("type for iterator is not recognized: " + typeGiven.toString());
    }
    return elemType;
  }

  public Type getIteratorType() {
    if (iteratorType == null) {
      throw new AstParseException("type for iterator is not recognized: " + typeGiven.toString());
    }
    return iteratorType;
  }

  private boolean checkIsIterable(final Type type) {

    /// we 100% sure that we can iterate over something if:
    /// 0) for item in 'collection' -> collection should be a class or an array
    /// 1) it has a method with a name 'get_iterator'
    /// 2) this method has no parameters
    /// 3) this method should return class-type
    /// 4) the returned class type should have three methods:
    ///    func has_next() -> (return boolean)
    ///    func get_next() -> (return the type of element in colection)
    /// 5) these methods shouldn't have parameters

    final ArrayList<ExprExpression> emptyArgs = new ArrayList<>();
    if (!type.is_class()) {
      return false; // TODO: arrays
    }

    // check that we have a method which will return the iterator
    //
    final ClassDeclaration clazz = type.getClassTypeFromRef();
    final ClassMethodDeclaration mGetIterator = clazz.getMethod(BuiltinNames.get_iterator_ident, emptyArgs);
    if (mGetIterator == null) {
      return false;
    }
    if (mGetIterator.isVoid()) {
      return false;
    }

    // check the iterator class
    //
    final Type returnTypeOfGetIteratorMethod = mGetIterator.getType();
    if (!returnTypeOfGetIteratorMethod.is_class()) {
      return false;
    }

    final ClassDeclaration iteratorClazz = returnTypeOfGetIteratorMethod.getClassTypeFromRef();
    final ClassMethodDeclaration mHasNext = iteratorClazz.getMethod(BuiltinNames.has_next_ident, emptyArgs);
    final ClassMethodDeclaration mGetNext = iteratorClazz.getMethod(BuiltinNames.get_next_ident, emptyArgs);
    if (mHasNext == null || mGetNext == null) {
      return false;
    }
    if (mHasNext.isVoid() || mGetNext.isVoid()) {
      return false;
    }

    if (mHasNext.getType().getBase() != TypeBase.TP_boolean) {
      return false;
    }

    this.elemType = mGetNext.getType();
    this.iteratorType = returnTypeOfGetIteratorMethod;
    return true;
  }
}
