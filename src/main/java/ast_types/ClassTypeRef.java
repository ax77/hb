package ast_types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import utils_oth.NullChecker;

public class ClassTypeRef implements Serializable {
  private static final long serialVersionUID = 7267823355463707870L;

  /// instantiated class: [new list<i32>();]
  /// where 'list' is a class with its type-parameters: class list<T> {  }
  /// and type-arguments are 'real' given types we have to expand in templates
  ///
  private final ClassDeclaration clazz;
  private /*final*/ List<Type> typeArguments;

  public ClassTypeRef(ClassDeclaration clazz, List<Type> typeArguments) {
    NullChecker.check(clazz, typeArguments);

    this.clazz = clazz;
    this.typeArguments = typeArguments;
  }

  public ClassDeclaration getClazz() {
    return clazz;
  }

  public List<Type> getTypeArguments() {
    return typeArguments;
  }

  public void putTypeArgument(Type e) {
    NullChecker.check(e);

    this.typeArguments.add(e);
  }

  public void forgetTemplateHistory() {
    this.typeArguments = new ArrayList<>();
    clazz.setTypeParametersT(new ArrayList<>());
  }

  public boolean is_equal_to(ClassTypeRef another) {
    if (this == another) {
      return true;
    }
    if (!clazz.is_equal_to(another.getClazz())) {
      return false;
    }

    if (!TypeListsComparer.typeListsAreEqual(typeArguments, another.getTypeArguments())) {
      return false;
    }

    return true;
  }

  @Override
  public String toString() {
    return clazz.getIdentifier().getName();
  }

  public boolean isTemplate() {
    return clazz.isTemplate();
  }

}
