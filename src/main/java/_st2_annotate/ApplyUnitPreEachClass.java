package _st2_annotate;

import java.io.IOException;

import ast_class.ClassDeclaration;
import ast_unit.InstantiationUnit;
import errors.AstParseException;

public class ApplyUnitPreEachClass {

  private final SymbolTable symtabApplier;
  private final InstantiationUnit instantiationUnit;

  public ApplyUnitPreEachClass(SymbolTable symtabApplier, InstantiationUnit instantiationUnit) {
    this.symtabApplier = symtabApplier;
    this.instantiationUnit = instantiationUnit;
  }

  public void preEachClass() throws IOException {
    for (ClassDeclaration c : instantiationUnit.getClasses()) {
      checkClazz(c);
      symtabApplier.defineClazz(c);
      addDefaultMethods(c);
    }
  }

  private void checkClazz(ClassDeclaration object) {

    if (object.isMainClass()) {
      return;
    }

    if (object.isNativeArray() || object.isNativeString()) {
      return;
    }

    if (object.getConstructors().isEmpty()) {
      throw new AstParseException("class has no constructor: " + object.getIdentifier().toString());
    }

    if (object.getFields().isEmpty()) {
      throw new AstParseException("class has no fields: " + object.getIdentifier().toString());
    }

  }

  private void addDefaultMethods(ClassDeclaration object) throws IOException {

  }

}
