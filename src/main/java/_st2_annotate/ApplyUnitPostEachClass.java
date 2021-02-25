package _st2_annotate;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_symtab.BuiltinNames;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_unit.InstantiationUnit;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;

public class ApplyUnitPostEachClass {

  private final SymbolTable symtabApplier;
  private final InstantiationUnit instantiationUnit;

  public ApplyUnitPostEachClass(SymbolTable symtabApplier, InstantiationUnit instantiationUnit) {
    this.symtabApplier = symtabApplier;
    this.instantiationUnit = instantiationUnit;
  }

  public void postEachClass() {
    for (ClassDeclaration c : instantiationUnit.getClasses()) {
      applyMethodsSpecial(c);
    }
  }

  private void applyMethodsSpecial(ClassDeclaration object) {
    addThisParamToEachMethod(object);
  }

  private void addThisParamToEachMethod(ClassDeclaration object) {
    for (ClassMethodDeclaration method : object.getMethods()) {
      method.pushParameterFront(createThisParameter(object, method));
    }

    for (ClassMethodDeclaration constructor : object.getConstructors()) {
      constructor.pushParameterFront(createThisParameter(object, constructor));
    }

    final ClassMethodDeclaration destructor = object.getDestructor();
    destructor.pushParameterFront(createThisParameter(object, destructor));
  }

  private VarDeclarator createThisParameter(ClassDeclaration object, ClassMethodDeclaration method) {
    //@formatter:off
    final Type paramType = new Type(new ClassTypeRef(object, object.getTypeParametersT()), object.getBeginPos());
    final VarDeclarator __thisParam = new VarDeclarator(VarBase.METHOD_PARAMETER
        , Mods.letMods()
        , paramType
        , BuiltinNames.__this_ident
        , method.getBeginPos()
    );
    return __thisParam;
    //@formatter:on
  }

}
