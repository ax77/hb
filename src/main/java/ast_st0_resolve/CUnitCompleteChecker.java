package ast_st0_resolve;

import ast_class.ClassDeclaration;
import ast_unit.CompilationUnit;
import errors.AstParseException;

public abstract class CUnitCompleteChecker {

  public static void checkAllClassesAreComplete(final CompilationUnit unit) {
    for (ClassDeclaration clazz : unit.getClasses()) {
      if (!clazz.isComplete()) {
        throw new AstParseException("incomplete class: " + clazz.getIdentifier());
      }
    }
    for (ClassDeclaration clazz : unit.getTemplates()) {
      if (!clazz.isComplete()) {
        throw new AstParseException("incomplete template: " + clazz.getIdentifier());
      }
    }
    for (ClassDeclaration clazz : unit.getForwards()) {
      if (!clazz.isComplete()) {
        throw new AstParseException("unused forward: " + clazz.getIdentifier());
      }
    }
  }

}
