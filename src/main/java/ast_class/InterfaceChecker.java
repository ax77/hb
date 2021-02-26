package ast_class;

import ast_types.Type;
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
}
