package ast_st2_annotate;

import ast_builtins.BuiltinNames;
import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
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
    //methods
    for (ClassMethodDeclaration method : object.getMethods()) {
      if (method.getIdentifier().equals(BuiltinNames.opAssign_ident)) {
        continue;
      }

      method.pushParameterFront(createThisParameter(object, method));
    }

    //constructors 
    for (ClassMethodDeclaration constructor : object.getConstructors()) {
      constructor.pushParameterFront(createThisParameter(object, constructor));
    }

    //destructor
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
