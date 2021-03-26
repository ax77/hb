package _st2_annotate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_stmt.StmtBlock;
import ast_stmt.StmtStatement;
import ast_symtab.BuiltinNames;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeBindings;
import ast_unit.InstantiationUnit;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import hashed.Hash_ident;

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

    if (!object.isStaticClass()) {
      for (VarDeclarator field : object.getFields()) {
        if (field.getSimpleInitializer() != null) {
          throw new AstParseException("field initializer unexpected: " + object.getIdentifier().toString() + "."
              + field.getIdentifier().toString());
        }
      }

      if (object.getConstructors().isEmpty() && !object.getModifiers().isNativeOnly()) {
        throw new AstParseException("class has no constructor: " + object.getIdentifier().toString());
      }

      if (object.getFields().isEmpty() && !object.getModifiers().isNativeOnly()) {
        throw new AstParseException("class has no fields: " + object.getIdentifier().toString());
      }
    }

    if (object.isStaticClass()) {
      for (VarDeclarator field : object.getFields()) {
        if (!field.getMods().isStatic()) {
          throw new AstParseException("field in static class should be static: " + object.getIdentifier().toString()
              + "." + field.getIdentifier().toString());
        }
        if (field.getSimpleInitializer() == null) {
          throw new AstParseException("field in static class is not initialized: " + object.getIdentifier().toString()
              + "." + field.getIdentifier().toString());
        }
      }
      for (ClassMethodDeclaration method : object.getMethods()) {
        if (!method.getModifiers().isStatic() && !method.getModifiers().isNative()) {
          throw new AstParseException("method in static class should be static: " + object.getIdentifier().toString()
              + "." + method.getIdentifier().toString());
        }
      }
      if (!object.getConstructors().isEmpty()) {
        throw new AstParseException("unexpected constructor in static class: " + object.getIdentifier().toString());
      }
    }

  }

  private void addDefaultMethods(ClassDeclaration object) throws IOException {

    //TODO: why does not this work? hmm?
    if (object.isMainClass()) {
      //return;
    }
    if (object.isStaticClass()) {
      //return;
    }

    if (!object.hasPredefinedMethod(BuiltinNames.equals_ident)) {

      List<VarDeclarator> parameters = new ArrayList<>();
      parameters.add(new VarDeclarator(VarBase.METHOD_PARAMETER, new Modifiers(),
          new Type(new ClassTypeRef(object, object.getTypeParametersT())), Hash_ident.getHashedIdent("another"),
          object.getBeginPos()));

      ClassMethodDeclaration m = new ClassMethodDeclaration(ClassMethodBase.IS_FUNC, new Modifiers(), object,
          BuiltinNames.equals_ident, parameters, TypeBindings.make_boolean(), new StmtBlock(), object.getBeginPos());

      object.addMethod(m);
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
