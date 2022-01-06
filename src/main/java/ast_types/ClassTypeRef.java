package ast_types;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_printers.ArgsListToString;
import utils_oth.NullChecker;

public class ClassTypeRef implements Serializable {
  private static final long serialVersionUID = 7267823355463707870L;

  /// instantiated class: [new list<i32>();]
  /// where 'list' is a class with its type-parameters: class list<T> {  }
  /// and type-arguments are 'real' given types we have to expand in templates
  ///
  private final ClassDeclaration clazz;
  private final List<Type> typeArguments;

  public ClassTypeRef(ClassDeclaration clazz, List<Type> typeArguments) {
    NullChecker.check(clazz, typeArguments);

    this.clazz = clazz;
    this.typeArguments = Collections.unmodifiableList(typeArguments);
  }

  public ClassDeclaration getClazz() {
    return clazz;
  }

  public List<Type> getTypeArguments() {
    return typeArguments;
  }

  public boolean isEqualTo(ClassTypeRef another) {
    if (this == another) {
      return true;
    }
    if (!clazz.isEqualTo(another.getClazz())) {
      return false;
    }

    if (!TypeListsComparer.typeListsAreEqual(typeArguments, another.getTypeArguments())) {
      return false;
    }

    return true;
  }

  @Override
  public String toString() {

    // we need to provide a simple class name, before code-generation.
    // it looks horrible in error printing.
    //

    StringBuilder sb = new StringBuilder();
    sb.append(clazz.getIdentifier().getName());
    if (!typeArguments.isEmpty()) {
      sb.append(ArgsListToString.paramsToStringWithBraces(typeArguments, '<'));
    }

    return sb.toString();
  }

  public boolean isTemplate() {
    return clazz.isTemplate();
  }

  public boolean isNamespace() {
    return clazz.isNamespace();
  }

  public boolean isInterface() {
    return clazz.isInterface();
  }

  public boolean isEnum() {
    return clazz.isEnum();
  }

}
