package _st2_annotate;

import java.io.IOException;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_stmt.StmtStatement;
import ast_unit.InstantiationUnit;
import ast_vars.VarDeclarator;

public class ApplyUnit {

  private final SymbolTable symtabApplier;
  private final InstantiationUnit instantiationUnit;

  public ApplyUnit(InstantiationUnit instantiationUnit) throws IOException {
    this.symtabApplier = new SymbolTable();
    this.instantiationUnit = instantiationUnit;
  }

  public void visit() throws IOException {
    symtabApplier.openFileScope();
    preEachClass();
    eachClass();
    postEachClass();
    symtabApplier.closeFileScope();
  }

  private void preEachClass() throws IOException {
    ApplyUnitPreEachClass applier = new ApplyUnitPreEachClass(symtabApplier, instantiationUnit);
    applier.preEachClass();
  }

  private void eachClass() throws IOException {
    for (ClassDeclaration c : instantiationUnit.getClasses()) {
      applyClazz(c);
    }
  }

  private void postEachClass() {
    ApplyUnitPostEachClass applier = new ApplyUnitPostEachClass(symtabApplier, instantiationUnit);
    applier.postEachClass();
  }

  private void applyClazz(final ClassDeclaration object) throws IOException {

    symtabApplier.openClassScope(object.getIdentifier().getName());

    //fields
    for (VarDeclarator field : object.getFields()) {
      symtabApplier.defineClassField(object, field); // check redefinition
    }

    //methods
    for (ClassMethodDeclaration method : object.getMethods()) {
      applyMethod(object, method);
    }

    //constructors 
    for (ClassMethodDeclaration constructor : object.getConstructors()) {
      applyMethod(object, constructor);
    }

    //destructor
    if (object.getDestructor() != null) {
      applyMethod(object, object.getDestructor());
    }

    symtabApplier.closeClassScope();
  }

  private void applyMethod(final ClassDeclaration object, final ClassMethodDeclaration method) {

    StringBuilder sb = new StringBuilder();
    sb.append(object.getIdentifier().getName());
    sb.append("_");
    sb.append(method.getBase().toString());
    sb.append("_");
    sb.append(method.getUniqueIdToString());

    symtabApplier.openMethodScope(sb.toString(), method);

    // parameters
    for (VarDeclarator fp : method.getParameters()) {
      symtabApplier.defineFunctionParameter(method, fp);
    }

    // block
    for (StmtStatement item : method.getBlock().getBlockItems()) {
      ApplyStatement applier = new ApplyStatement(symtabApplier);
      applier.applyStatement(object, method, item);
    }

    symtabApplier.closeMethodScope();
  }

}
