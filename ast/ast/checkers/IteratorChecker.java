package ast.ast.checkers;

import static ast.ast.mir.Renamer.GET_ITERATOR_METHOD_NAME;
import static ast.ast.mir.Renamer.ITERATOR_GET_CURRENT_METHOD_NAME;
import static ast.ast.mir.Renamer.ITERATOR_GET_NEXT_METHOD_NAME;
import static ast.ast.mir.Renamer.ITERATOR_HAS_NEXT_METHOD_NAME;

import java.util.ArrayList;

import ast.ast.nodes.ClassDeclaration;
import ast.ast.nodes.expr.FuncArg;
import ast.ast.nodes.method.ClassMethodDeclaration;
import ast.parse.AstParseException;
import ast.types.Type;
import ast.types.TypeBase;

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
    ///    func current() -> (return the type of element in colection)
    ///    func has_next() -> (return boolean)
    ///    func get_next() -> (return the type of element in colection)
    /// 5) these methods shouldn't have parameters

    final ArrayList<FuncArg> emptyArgs = new ArrayList<>();
    if (!type.is_class()) {
      return false; // TODO: arrays
    }

    // check that we have a method which will return the iterator
    //
    final ClassDeclaration clazz = type.getClassType();
    final ClassMethodDeclaration mGetIterator = clazz.getMethod(GET_ITERATOR_METHOD_NAME, emptyArgs);
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

    final ClassDeclaration iteratorClazz = returnTypeOfGetIteratorMethod.getClassType();
    final ClassMethodDeclaration mCurrent = iteratorClazz.getMethod(ITERATOR_GET_CURRENT_METHOD_NAME, emptyArgs);
    final ClassMethodDeclaration mHasNext = iteratorClazz.getMethod(ITERATOR_HAS_NEXT_METHOD_NAME, emptyArgs);
    final ClassMethodDeclaration mGetNext = iteratorClazz.getMethod(ITERATOR_GET_NEXT_METHOD_NAME, emptyArgs);
    if (mCurrent == null || mHasNext == null || mGetNext == null) {
      return false;
    }
    if (mCurrent.isVoid() || mHasNext.isVoid() || mGetNext.isVoid()) {
      return false;
    }

    if (mHasNext.getType().getBase() != TypeBase.TP_BOOLEAN) {
      return false;
    }

    // check what the type will return our iterator we found
    //
    final Type returnTypeGetCurrent = mCurrent.getType();
    final Type returnTypeGetNext = mGetNext.getType();
    if (!returnTypeGetCurrent.is_equal_to(returnTypeGetNext)) {
      return false;
    }

    this.elemType = returnTypeGetNext;
    this.iteratorType = returnTypeOfGetIteratorMethod;
    return true;
  }
}
