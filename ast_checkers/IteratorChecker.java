package njast.ast_checkers;

import java.util.ArrayList;

import jscan.hashed.Hash_ident;
import jscan.symtab.Ident;
import njast.ast_nodes.FuncArg;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.errors.EParseException;
import njast.types.Type;
import njast.types.TypeBase;

public class IteratorChecker {

  private final VarDeclarator var;
  private final boolean isIterable;
  private Type elemType;

  public IteratorChecker(VarDeclarator var) {
    this.var = var;
    this.isIterable = checkIsIterable();
  }

  public boolean isIterable() {
    return isIterable;
  }

  public Type getElemType() {
    if (elemType == null) {
      throw new EParseException("type for iterator is not recognized: " + var.toString());
    }
    return elemType;
  }

  private boolean checkIsIterable() {

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
    final Ident getIteratorIdent = Hash_ident.getHashedIdent("get_iterator");
    final Ident getCurrentIdent = Hash_ident.getHashedIdent("current");
    final Ident hasNextIdent = Hash_ident.getHashedIdent("has_next");
    final Ident getNextIdent = Hash_ident.getHashedIdent("get_next");

    final Type type = var.getType();
    if (!type.isClassRef()) {
      return false; // TODO: arrays
    }

    // check that we have a method which will return the iterator
    //
    final ClassDeclaration clazz = type.getClassType();
    final ClassMethodDeclaration mGetIterator = clazz.getMethod(getIteratorIdent, emptyArgs);
    if (mGetIterator == null) {
      return false;
    }
    if (mGetIterator.isVoid()) {
      return false;
    }

    // check the iterator class
    //
    final Type returnTypeOfGetIteratorMethod = mGetIterator.getType();
    if (!returnTypeOfGetIteratorMethod.isClassRef()) {
      return false;
    }

    final ClassDeclaration iteratorClazz = returnTypeOfGetIteratorMethod.getClassType();
    final ClassMethodDeclaration mCurrent = iteratorClazz.getMethod(getCurrentIdent, emptyArgs);
    final ClassMethodDeclaration mHasNext = iteratorClazz.getMethod(hasNextIdent, emptyArgs);
    final ClassMethodDeclaration mGetNext = iteratorClazz.getMethod(getNextIdent, emptyArgs);
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
    if (!returnTypeGetCurrent.isEqualTo(returnTypeGetNext)) {
      return false;
    }

    this.elemType = returnTypeGetNext;
    return true;
  }
}
