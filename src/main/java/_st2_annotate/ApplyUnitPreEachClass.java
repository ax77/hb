
package _st2_annotate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprBinary;
import ast_expr.ExprBuiltinFunc;
import ast_expr.ExprDefaultValueForType;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_expr.ExprUnary;
import ast_method.ClassMethodBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_stmt.StmtBlock;
import ast_stmt.StmtReturn;
import ast_stmt.StmtSelect;
import ast_stmt.StmtStatement;
import ast_symtab.BuiltinNames;
import ast_symtab.Keywords;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeBindings;
import ast_unit.InstantiationUnit;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import hashed.Hash_ident;
import tokenize.Ident;
import tokenize.T;
import tokenize.Token;

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

    if (!object.isNamespace()) {
      checkPlainClassSem(object);
    }

    if (object.isNamespace()) {
      checkNamespace(object);
    }

  }

  private void checkPlainClassSem(ClassDeclaration object) {

    for (VarDeclarator field : object.getFields()) {
      if (field.getSimpleInitializer() != null) {
        throw new AstParseException("field initializer unexpected: " + object.getIdentifier().toString() + "."
            + field.getIdentifier().toString());
      }
      if (field.getType().isNamespace()) {
        throw new AstParseException("field cannot be a namespace name type: " + object.getIdentifier().toString() + "."
            + field.getIdentifier().toString());
      }
    }

    if (object.getConstructors().isEmpty() && !object.getModifiers().isNativeOnly() && !object.isInterface()) {
      //XXX:default_constructor
      //throw new AstParseException("class has no constructor: " + object.getIdentifier().toString());
    }

    if (object.getFields().isEmpty() && !object.getModifiers().isNativeOnly() && !object.isInterface()) {
      throw new AstParseException("class has no fields: " + object.getIdentifier().toString());
    }

  }

  private void checkNamespace(ClassDeclaration object) {

    // we cannot use fields in a static class.
    // it has no constructors, destructors, etc.
    // how can we free the class?
    // but: we have to use [public static final type varname = value;] expressions.
    // we need these constants, and we will generate simple c-code using static data.

    for (VarDeclarator field : object.getFields()) {
      final String msg = object.getIdentifier().toString() + "." + field.getIdentifier().toString();

      if (field.getSimpleInitializer() == null) {
        throw new AstParseException("field in static class is not initialized: " + msg);
      }
      if (!field.getType().isPrimitive()) {
        throw new AstParseException("field in static class can only be a primitive type: " + msg);
      }
    }

    if (!object.getConstructors().isEmpty()) {
      throw new AstParseException("unexpected constructor in namespace: " + object.getIdentifier().toString());
    }

    if (object.getDestructor() != null) {
      throw new AstParseException("unexpected destructor in namespace: " + object.getIdentifier().toString());
    }

  }

  private void addDefaultMethods(ClassDeclaration object) throws IOException {

    if (object.isMainClass()) {
      return;
    }

    // TODO:static_semantic
    if (object.isNamespace()) {
      return;
    }

    addEqualsMethod(object);
    addDestructor(object);
    addConstructor(object);

  }

  private void addEqualsMethod(ClassDeclaration object) {
    if (!object.hasPredefinedMethod(BuiltinNames.equals_ident)) {

      final Token beginPos = object.getBeginPos();
      List<VarDeclarator> parameters = new ArrayList<>();

      final Ident anotherIdent = Hash_ident.getHashedIdent("another");
      parameters.add(new VarDeclarator(VarBase.METHOD_PARAMETER, new Modifiers(),
          new Type(new ClassTypeRef(object, object.getTypeParametersT())), anotherIdent, beginPos));

      /// return this == another
      final StmtBlock block = new StmtBlock();
      ExprExpression lhs = new ExprExpression(object, beginPos);
      ExprExpression rhs = new ExprExpression(new ExprIdent(anotherIdent), beginPos);
      ExprExpression eq = new ExprExpression(new ExprBinary(new Token(beginPos, "==", T.T_EQ), lhs, rhs), beginPos);
      StmtReturn ret = new StmtReturn();
      ret.setExpression(eq);
      block.pushItemBack(new StmtStatement(ret, beginPos));

      ClassMethodDeclaration m = new ClassMethodDeclaration(ClassMethodBase.IS_FUNC, new Modifiers(), object,
          BuiltinNames.equals_ident, parameters, TypeBindings.make_boolean(), block, beginPos);

      m.setGeneratedByDefault();
      object.addMethod(m);
    }

    else {
      checkEqualsMethodSignature(object);
    }
  }

  private void checkEqualsMethodSignature(ClassDeclaration object) {
    // TODO Auto-generated method stub

  }

  private void addDestructor(ClassDeclaration object) {
    if (object.getDestructor() == null) {
      final ClassMethodDeclaration defaultDestructor = BuildDefaultDestructor.build(object);
      addGuardFront(defaultDestructor);
      object.setDestructor(defaultDestructor);
    } else {
      //TODO:
      //addGuardFront(object.getDestructor());
      //addGuardBack(object.getDestructor());
    }
  }

  private void addGuardFront(ClassMethodDeclaration destructor) {
    final ClassDeclaration object = destructor.getClazz();
    final Token beginPos = object.getBeginPos();
    final ExprExpression idExpr = new ExprExpression(object, beginPos);
    final List<ExprExpression> fnamearg = new ArrayList<>();
    fnamearg.add(idExpr);

    final StmtBlock trueStatement = new StmtBlock();
    trueStatement.pushItemBack(new StmtStatement(new StmtReturn(), beginPos));

    // check whether it has deletion bit

    final ExprBuiltinFunc hasBit = new ExprBuiltinFunc(Keywords.is_alive_ident, fnamearg);

    final ExprUnary nothas = new ExprUnary(new Token("!", T.T_EXCLAMATION, beginPos.getLocation()),
        new ExprExpression(hasBit, beginPos));

    StmtStatement select2 = new StmtStatement(new StmtSelect(new ExprExpression(nothas, beginPos), trueStatement, null),
        beginPos);

    // hide the generated code in {  }
    final StmtBlock guardBlock = new StmtBlock();
    guardBlock.pushItemBack(select2);

    final ExprBuiltinFunc setBit = new ExprBuiltinFunc(Keywords.set_deletion_bit_ident, fnamearg);
    guardBlock.pushItemBack(new StmtStatement(new ExprExpression(setBit, beginPos), beginPos));

    destructor.getBlock().pushItemFront(new StmtStatement(guardBlock, beginPos));
  }

  private void addGuardBack(ClassMethodDeclaration destructor) {
    final ClassDeclaration object = destructor.getClazz();
    final Type type = new Type(new ClassTypeRef(object, destructor.getClazz().getTypeParametersT()));

    final Token beginPos = object.getBeginPos();
    final ExprExpression idExpr = new ExprExpression(object, beginPos);
    ExprDefaultValueForType defaultValueForType = new ExprDefaultValueForType(type);

    // check whether it has deletion bit
    List<ExprExpression> fnamearg = new ArrayList<>();
    fnamearg.add(idExpr);
    fnamearg.add(new ExprExpression(defaultValueForType, beginPos));

    /// mark __this itself
    ExprBuiltinFunc setBit = new ExprBuiltinFunc(Keywords.set_deletion_bit_ident, fnamearg);
    final StmtStatement item = new StmtStatement(new ExprExpression(setBit, beginPos), beginPos);

    // hide the generated code in {  }
    final StmtBlock guardBlock = new StmtBlock();
    guardBlock.pushItemBack(item);

    destructor.getBlock().pushItemBack(new StmtStatement(guardBlock, beginPos));
  }

  private void addConstructor(ClassDeclaration object) {
    if (object.getConstructors().isEmpty()) {
      object.addConstructor(BuildDefaultConstructor.build(object));
    } else {
      /// We have to initialize all of the fields with their default values.
      /// And this statement must be the first in the constructor block.
      /// After this statement a normal initizlizers will follow.
      for (ClassMethodDeclaration constr : object.getConstructors()) {
        constr.getBlock().pushItemFront(new StmtStatement(
            BuildDefaultConstructor.genBlockForWithAllFieldsAreInitialized(object), object.getBeginPos()));
      }
    }

  }

}
