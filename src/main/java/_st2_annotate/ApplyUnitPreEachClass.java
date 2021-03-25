package _st2_annotate;

import java.io.IOException;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodDeclaration;
import ast_stmt.StmtStatement;
import ast_unit.InstantiationUnit;
import ast_vars.VarDeclarator;
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

    if (!object.isStaticClass()) {
      for (VarDeclarator field : object.getFields()) {
        if (field.getSimpleInitializer() != null) {
          throw new AstParseException("field initializer unexpected: " + object.getIdentifier().toString() + "."
              + field.getIdentifier().toString());
        }
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
        throw new AstParseException("constructor in static class not expected: " + object.getIdentifier().toString());
      }
    }

    if (object.getConstructors().isEmpty() && !object.getModifiers().isStaticOnly()
        && !object.getModifiers().isNativeOnly()) {
      throw new AstParseException("class has no constructor: " + object.getIdentifier().toString());
    }

    if (object.getFields().isEmpty() && !object.getModifiers().isStaticOnly()
        && !object.getModifiers().isNativeOnly()) {
      throw new AstParseException("class has no fields: " + object.getIdentifier().toString());
    }

  }

  private void addDefaultMethods(ClassDeclaration object) throws IOException {
    if (object.getDestructor() == null) {
      object.setDestructor(BuildDefaultDestructor.build(object));
    } else {
      for (StmtStatement s : BuildDefaultDestructor.deinits(object)) {
        object.getDestructor().getBlock().pushItemBack(s);
      }
    }
  }

}
