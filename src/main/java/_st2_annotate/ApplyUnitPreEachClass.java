package _st2_annotate;

import java.io.IOException;

import ast_class.ClassDeclaration;
import ast_stmt.StmtStatement;
import ast_symtab.BuiltinNames;
import ast_unit.InstantiationUnit;

public class ApplyUnitPreEachClass {

  private final SymbolTable symtabApplier;
  private final InstantiationUnit instantiationUnit;

  public ApplyUnitPreEachClass(SymbolTable symtabApplier, InstantiationUnit instantiationUnit) {
    this.symtabApplier = symtabApplier;
    this.instantiationUnit = instantiationUnit;
  }

  public void preEachClass() throws IOException {
    for (ClassDeclaration c : instantiationUnit.getClasses()) {
      symtabApplier.defineClazz(c);
      addDefaultMethods(c);
    }
  }

  private void addDefaultMethods(ClassDeclaration object) throws IOException {

    if (object.getConstructors().isEmpty()) {
      object.addConstructor(BuildDefaultConstructor.build(object));
    }

    if (object.getDestructor() == null) {
      object.setDestructor(BuildDefaultDestructor.build(object));
    } else {
      for (StmtStatement s : BuildDefaultDestructor.deinits(object)) {
        object.getDestructor().getBlock().pushItemBack(s);
      }
    }

  }

}
