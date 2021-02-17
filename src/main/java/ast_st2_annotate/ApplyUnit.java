package ast_st2_annotate;

import java.util.ArrayList;

import ast_class.ClassDeclaration;
import ast_method.ClassMethodBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
import ast_stmt.StmtStatement;
import ast_types.Type;
import ast_unit.InstantiationUnit;
import ast_vars.VarDeclarator;
import tokenize.Token;

public class ApplyUnit {

  private final SymbolTable symtabApplier;

  public ApplyUnit() {
    this.symtabApplier = new SymbolTable();
  }

  public void visit(InstantiationUnit o) {
    symtabApplier.openFileScope();
    for (ClassDeclaration td : o.getClasses()) {
      symtabApplier.defineClazz(td);
    }
    for (ClassDeclaration td : o.getClasses()) {
      applyClazz(td);
    }
    symtabApplier.closeFileScope();
  }

  private void applyClazz(final ClassDeclaration object) {

    symtabApplier.openClassScope(object.getIdentifier().getName());

    addDefaultMethods(object);

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

  private void addDefaultMethods(ClassDeclaration object) {
    final StmtBlock block = new StmtBlock();
    final Token beginPos = object.getBeginPos();

    if (object.getConstructors().isEmpty()) {
      final ArrayList<VarDeclarator> emptyParams = new ArrayList<>();
      final Type voidType = new Type(beginPos);
      final Modifiers emptyMods = new Modifiers();
      final ClassMethodDeclaration constructor = new ClassMethodDeclaration(ClassMethodBase.IS_CONSTRUCTOR, emptyMods,
          object, object.getIdentifier(), emptyParams, voidType, block, beginPos);

      constructor.setGeneratedByDefault();
      object.addConstructor(constructor);
    }

    if (object.getDestructor() == null) {
      final ClassMethodDeclaration destructor = new ClassMethodDeclaration(object, block, beginPos);

      destructor.setGeneratedByDefault();
      object.addConstructor(destructor);
    }

  }

  private void applyMethod(final ClassDeclaration object, final ClassMethodDeclaration method) {

    StringBuilder sb = new StringBuilder();
    sb.append(object.getIdentifier().getName());
    sb.append("_");
    sb.append(method.getBase().toString());
    sb.append("_");
    sb.append(method.getUniqueIdToString());

    symtabApplier.openMethodScope(sb.toString(), method);

    if (!method.isDestructor()) {
      for (VarDeclarator fp : method.getParameters()) {
        symtabApplier.defineFunctionParameter(method, fp);
      }
    }

    //body
    final StmtBlock body = method.getBlock();
    for (StmtBlockItem block : body.getBlockItems()) {

      // method variables
      final VarDeclarator var = block.getLocalVariable();
      if (var != null) {
        symtabApplier.defineMethodVariable(method, var);
        applyInitializer(object, var);
      }

      applyStatement(object, method, block.getStatement());
    }

    symtabApplier.closeMethodScope();
  }

  private void applyStatement(ClassDeclaration object, ClassMethodDeclaration method, StmtStatement statement) {
    ApplyStatement applier = new ApplyStatement(symtabApplier);
    applier.applyStatement(object, method, statement);
  }

  private void applyInitializer(ClassDeclaration object, VarDeclarator var) {
    ApplyInitializer applier = new ApplyInitializer(symtabApplier);
    applier.applyInitializer(object, var);

  }

}
