
package _st2_annotate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprAssign;
import ast_expr.ExprBinary;
import ast_expr.ExprDefaultValueForType;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_method.ClassMethodBase;
import ast_method.ClassMethodDeclaration;
import ast_modifiers.Modifiers;
import ast_stmt.StmtBlock;
import ast_stmt.StmtFor;
import ast_stmt.StmtReturn;
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
      final String msg = object.getIdentifier().toString() + "." + field.getIdentifier().toString();

      if (!field.getMods().isStatic()) {
        throw new AstParseException("field in static class must be static: " + msg);
      }
      if (field.getSimpleInitializer() == null) {
        throw new AstParseException("field in static class is not initialized: " + msg);
      }
      if (!field.getType().isPrimitive()) {
        throw new AstParseException("field in static class can only be a primitive type: " + msg);
      }
    }

    for (ClassMethodDeclaration method : object.getMethods()) {
      boolean isOk = method.getModifiers().isStatic() || method.getModifiers().isNative();
      if (!isOk) {
        throw new AstParseException("method in static class must only be static or native: "
            + object.getIdentifier().toString() + "." + method.getIdentifier().toString());
      }
    }
    if (!object.getConstructors().isEmpty()) {
      throw new AstParseException("unexpected constructor in static class: " + object.getIdentifier().toString());
    }

  }

  private void addDefaultMethods(ClassDeclaration object) throws IOException {

    if (object.isMainClass()) {
      return;
    }

    // TODO:static_semantic
    if (object.isStaticClass()) {
      return;
    }

    addEqualsMethod(object);
    addDestructor(object);
    addConstructor(object);
    
    //addSetDeletionMarkMethod(object);
  }

  private void addSetDeletionMarkMethod(ClassDeclaration object) {
    if (object.hasPredefinedMethod(BuiltinNames.set_deletion_mark_ident)) {
      return;
    }

    /// void set_deletion_mark(boolean m) { 
    ///     this.field_1.set_deletion_mark(m);
    ///     this.field_2.set_deletion_mark(m);
    ///     this.field_3.set_deletion_mark(m);
    /// }

    final Token beginPos = object.getBeginPos();
    List<VarDeclarator> parameters = new ArrayList<>();

    final Ident mIdent = Hash_ident.getHashedIdent("m");
    VarDeclarator param = new VarDeclarator(VarBase.METHOD_PARAMETER, new Modifiers(), TypeBindings.make_boolean(),
        mIdent, beginPos);
    parameters.add(param);

    final List<ExprExpression> arguments = new ArrayList<>();
    arguments.add(new ExprExpression(new ExprIdent(mIdent), beginPos));

    final StmtBlock block = new StmtBlock();
    final List<VarDeclarator> fields = object.getFields();

    if (!fields.isEmpty()) {
      for (int i = fields.size() - 1; i >= 0; i -= 1) {
        VarDeclarator field = fields.get(i);

        Type tp = field.getType();
        Ident name = field.getIdentifier();

        if (!tp.isClass()) {
          continue;
        }

        ClassTypeRef ref = tp.getClassTypeRef();
        ClassDeclaration classTypeForField = ref.getClazz();

        if (classTypeForField.isEqualTo(object)) {
          StmtStatement loopInsideTheBlock = ExpandRecursiveCallIntoLoop.expandField(field);
          block.pushItemBack(loopInsideTheBlock);
        } else {
          invokeSetDeletionMarkForField(object, beginPos, arguments, block, name);
        }
      }
    }

    ClassMethodDeclaration m = new ClassMethodDeclaration(ClassMethodBase.IS_FUNC, new Modifiers(), object,
        BuiltinNames.set_deletion_mark_ident, parameters, new Type(beginPos), block, beginPos);

    m.setGeneratedByDefault();
    object.addMethod(m);
  }

  private void invokeSetDeletionMarkForField(ClassDeclaration object, final Token beginPos,
      final List<ExprExpression> arguments, final StmtBlock block, Ident name) {
    ExprExpression thisExpression = new ExprExpression(object, object.getBeginPos());
    ExprFieldAccess eFieldAccess = new ExprFieldAccess(thisExpression, name);
    ExprExpression lhs = new ExprExpression(eFieldAccess, object.getBeginPos());

    ExprMethodInvocation eMethodInvocation = new ExprMethodInvocation(lhs, BuiltinNames.set_deletion_mark_ident,
        arguments);

    block.pushItemBack(new StmtStatement(new ExprExpression(eMethodInvocation, beginPos), beginPos));
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
      object.setDestructor(BuildDefaultDestructor.build(object));
    } else {
      for (StmtStatement s : BuildDefaultDestructor.deinits(object)) {
        object.getDestructor().getBlock().pushItemBack(s);
      }
    }
  }

  private void addConstructor(ClassDeclaration object) {
    if (object.getConstructors().isEmpty()) {
      object.addConstructor(BuildDefaultConstructor.build(object));
    } else {
      for (ClassMethodDeclaration constr : object.getConstructors()) {
        constr.getBlock().pushItemFront(new StmtStatement(
            BuildDefaultConstructor.genBlockForWithAllFieldsAreInitialized(object), object.getBeginPos()));
      }
    }

  }

}
