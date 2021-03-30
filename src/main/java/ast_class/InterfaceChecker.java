package ast_class;

import ast_method.ClassMethodDeclaration;
import ast_types.Type;
import errors.AstParseException;
import errors.ErrorLocation;

public abstract class InterfaceChecker {

  public static void checkItIsAnInterfaceClass(Type interface_) {
    if (!interface_.isClass()) {
      ErrorLocation.errorType("expected interface, but was", interface_);
    }
    ClassDeclaration clazz = interface_.getClassTypeFromRef();
    if (!clazz.isInterface()) {
      ErrorLocation.errorType("expected interface, but was", interface_);
    }
  }

  public static boolean classFullyImplementsTheInterface(ClassDeclaration clazz, ClassDeclaration interf) {
    for (ClassMethodDeclaration m : interf.getMethods()) {
      ClassMethodDeclaration found = clazz.getMethodByParams(m.getIdentifier(), m.getParameters());
      if (found == null) {
        return false;
      }
    }
    return true;
  }

  public static void checkImplementations(ClassDeclaration clazz) {
    if (!clazz.isClass()) {
      throw new AstParseException("expecting class");
    }
    if (clazz.getInterfaces().isEmpty()) {
      return;
    }
    for (InterfaceItem intefr : clazz.getInterfaces()) {
      final ClassDeclaration classTypeFromRef = intefr.getType().getClassTypeFromRef();
      if (!classFullyImplementsTheInterface(clazz, classTypeFromRef)) {
        throw new AstParseException("the class is not implements the interface properly: "
            + clazz.getIdentifier().toString() + ": " + classTypeFromRef.getIdentifier().toString());
      }
    }
  }
}
