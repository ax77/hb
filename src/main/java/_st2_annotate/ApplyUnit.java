package _st2_annotate;

import java.io.IOException;
import java.util.List;

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
    ApplyUnitPostEachClass applier = new ApplyUnitPostEachClass(instantiationUnit);
    applier.postEachClass();
  }

  private void applyClazz(final ClassDeclaration object) throws IOException {

    symtabApplier.openClassScope();

    //fields
    for (VarDeclarator field : object.getFields()) {
      symtabApplier.defineClassField(object, field);
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

    //tests 
    for (ClassMethodDeclaration tst : object.getTests()) {
      applyMethod(object, tst);
    }

    symtabApplier.closeClassScope();
  }

  private void applyMethod(final ClassDeclaration object, final ClassMethodDeclaration method) {

    symtabApplier.openMethodScope(method);

    // parameters
    for (VarDeclarator fp : method.getParameters()) {
      symtabApplier.defineFunctionParameter(method, fp);
    }

    // block
    final List<StmtStatement> items = method.getBlock().getBlockItems();
    for (StmtStatement item : items) {
      ApplyStatement applier = new ApplyStatement(symtabApplier, object, method);
      applier.applyStatement(item);
    }

    symtabApplier.closeMethodScope();
  }

}
