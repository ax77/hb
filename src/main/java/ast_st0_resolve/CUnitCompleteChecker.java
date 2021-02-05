package ast_st0_resolve;

import java.util.List;

import ast_class.ClassDeclaration;
import ast_unit.CompilationUnit;
import errors.AstParseException;
import tokenize.Ident;

public abstract class CUnitCompleteChecker {

  public static void checkAllClassesAreComplete(final CompilationUnit unit) {
    checkList(unit.getClasses());
    checkList(unit.getTemplates());
    checkList(unit.getForwards());
  }

  private static void checkList(List<ClassDeclaration> classes) {
    for (ClassDeclaration clazz : classes) {
      if (!clazz.isComplete()) {
        errorIncomplete(clazz);
      }
    }
  }

  private static void errorIncomplete(ClassDeclaration clazz) {
    final Ident identifier = clazz.getIdentifier();
    final String loc = clazz.getLocationToString();
    throw new AstParseException(loc + ": error: unexpected incomplete: " + identifier.getName());
  }

}
