package ast.types;

import java.io.Serializable;
import java.util.List;

import ast.ast.nodes.ClassDeclaration;
import ast.parse.NullChecker;

public class ClassType implements Serializable {
  private static final long serialVersionUID = 7267823355463707870L;

  private final ClassDeclaration clazz;
  private final List<Type> typeArguments;

  public ClassType(ClassDeclaration clazz, List<Type> typeArguments) {
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

  public boolean is_equal_to(ClassType another) {
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
