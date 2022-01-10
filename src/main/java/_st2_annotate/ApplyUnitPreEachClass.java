
package _st2_annotate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprBinary;
import ast_expr.ExprBuiltinFunc;
import ast_expr.ExprExpression;
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
    if (!object.isStaticClass()) {
      checkPlainClassSem(object);
    }
    if (object.isStaticClass()) {
      checkStaticClassSem(object);
    }
  }

  private void checkPlainClassSem(ClassDeclaration object) {

    for (VarDeclarator field : object.getFields()) {
      if (field.getSimpleInitializer() != null) {
        throw new AstParseException("field initializer unexpected: " + object.getIdentifier().toString() + "."
            + field.getIdentifier().toString());
      }
      if (field.getType().isStaticClass()) {
        throw new AstParseException("field cannot be a static-class name type: " + object.getIdentifier().toString()
            + "." + field.getIdentifier().toString());
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

  private void checkStaticClassSem(ClassDeclaration object) {

    // we cannot use fields in a static class.
    // it has no constructors, destructors, etc.
    // how can we free the class?
    // but: we have to use [public static final type varname = value;] expressions.
    // we need these constants, and we will generate simple c-code using static data.

    for (VarDeclarator field : object.getFields()) {
      final String msg = object.getIdentifier().toString() + "." + field.getIdentifier().toString() + ", "
          + field.getLocationToString();

      if (field.getSimpleInitializer() == null) {
        throw new AstParseException("field in static class is not initialized: " + msg);
      }
      if (!field.getType().isPrimitive()) {
        throw new AstParseException("field in static class can only be a primitive type: " + msg);
      }
      if (!field.getModifiers().isStatic()) {
        throw new AstParseException("field in static class should be static: " + msg);
      }
    }

    for (ClassMethodDeclaration m : object.getMethods()) {
      final String msg = object.getIdentifier().toString() + "." + m.getIdentifier().toString() + ", "
          + m.getLocationToString();
      
      if (!m.getModifiers().isStatic() && !m.getModifiers().isNative()) {
        throw new AstParseException("method in static class should be static: " + msg);
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
    if (object.isStaticClass()) {
      return;
    }
    if (object.isEnum()) {
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
      addGuardFront(object.getDestructor());
      final StmtBlock emptifiers = BuildDefaultInitializersBlockForAllFields.createEmptifiiers(object);
      object.getDestructor().getBlock().pushItemBack(new StmtStatement(emptifiers, object.getBeginPos()));
    }
  }

  // if(!is_alive(__this)) {
  //     return;
  // }
  // set_deletion_bit(__this);

  private void addGuardFront(ClassMethodDeclaration destructor) {
    final ClassDeclaration object = destructor.getClazz();
    final Token beginPos = object.getBeginPos();
    final ExprExpression idExpr = new ExprExpression(object, beginPos);
    final List<ExprExpression> fnamearg = new ArrayList<>();
    fnamearg.add(idExpr);

    final StmtBlock trueStatement = new StmtBlock();
    trueStatement.pushItemBack(new StmtStatement(new StmtReturn(), beginPos));

    final ExprBuiltinFunc isAliveCheckCall = new ExprBuiltinFunc(Keywords.is_alive_ident, fnamearg);
    final ExprExpression operand = new ExprExpression(isAliveCheckCall, beginPos);
    final ExprUnary unary = new ExprUnary(new Token("!", T.T_EXCLAMATION, beginPos.getLocation()), operand);
    final ExprExpression ifNotAliveExpression = new ExprExpression(unary, beginPos);

    final StmtSelect ifStmt = new StmtSelect(ifNotAliveExpression, trueStatement, null);
    final StmtStatement ifIsNotAliveReturnStmt = new StmtStatement(ifStmt, beginPos);

    // hide the generated code in {  }
    final StmtBlock guardBlock = new StmtBlock();
    guardBlock.pushItemBack(ifIsNotAliveReturnStmt);

    final ExprBuiltinFunc setBit = new ExprBuiltinFunc(Keywords.set_deletion_bit_ident, fnamearg);
    guardBlock.pushItemBack(new StmtStatement(new ExprExpression(setBit, beginPos), beginPos));

    // push the whole guard to front
    destructor.getBlock().pushItemFront(new StmtStatement(guardBlock, beginPos));
  }

  private void addConstructor(ClassDeclaration object) {
    if (object.getConstructors().isEmpty()) {
      object.addConstructor(BuildDefaultConstructor.build(object));
    } else {
      /// We have to initialize all of the fields with their default values.
      /// And this statement must be the first in the constructor block.
      /// After this statement a normal initizlizers will follow.
      for (ClassMethodDeclaration constr : object.getConstructors()) {
        final StmtBlock emptifiers = BuildDefaultInitializersBlockForAllFields.createEmptifiiers(object);
        constr.getBlock().pushItemFront(new StmtStatement(emptifiers, object.getBeginPos()));
      }
    }

  }

}
