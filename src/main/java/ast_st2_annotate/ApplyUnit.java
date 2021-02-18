package ast_st2_annotate;

import java.util.ArrayList;
import java.util.List;

import ast_builtins.BuiltinNames;
import ast_class.ClassDeclaration;
import ast_expr.ExprExpression;
import ast_expr.ExprIdent;
import ast_method.ClassMethodBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_stmt.StmtBlock;
import ast_stmt.StmtBlockItem;
import ast_stmt.StmtReturn;
import ast_stmt.StmtStatement;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_unit.InstantiationUnit;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import hashed.Hash_ident;
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

    /// Test:ASSIGN_operator

    //@formatter:off
    final Type typename = new Type(new ClassTypeRef(object, object.getTypeParametersT()), beginPos);
    final List<VarDeclarator>parameters = new ArrayList<>();
    parameters.add(new VarDeclarator(VarBase.METHOD_PARAMETER, new Modifiers(), typename, Hash_ident.getHashedIdent("lvalue"), beginPos));
    parameters.add(new VarDeclarator(VarBase.METHOD_PARAMETER, new Modifiers(), typename, Hash_ident.getHashedIdent("rvalue"), beginPos));
    
    final StmtBlock assignBlock = new StmtBlock();
    final StmtReturn stmtReturn = new StmtReturn();
    final ExprExpression retExpr = new ExprExpression(new ExprIdent(Hash_ident.getHashedIdent("rvalue")), beginPos);
    stmtReturn.setExpression(retExpr);
    assignBlock.put(new StmtBlockItem(new StmtStatement(stmtReturn, beginPos)));
    final ClassMethodDeclaration opAssignMethod = new ClassMethodDeclaration(ClassMethodBase.IS_FUNC
        , new Modifiers()
        , object
        , BuiltinNames.opAssign_ident
        , parameters
        , typename
        , assignBlock
        , beginPos
    );
    object.addMethod(opAssignMethod);
    //@formatter:on

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
