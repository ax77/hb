package _st2_annotate;

import static _st2_annotate.SymbolTable.F_ALL;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprAlloc;
import ast_expr.ExprAssign;
import ast_expr.ExprBinary;
import ast_expr.ExprBuiltinFunc;
import ast_expr.ExprCast;
import ast_expr.ExprClassCreation;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprForLoopStepComma;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.ExprSizeof;
import ast_expr.ExprStaticAccess;
import ast_expr.ExprTernaryOperator;
import ast_expr.ExprTypeof;
import ast_expr.ExprUnary;
import ast_expr.ExpressionBase;
import ast_method.ClassMethodDeclaration;
import ast_printers.ArgsListToString;
import ast_symtab.BuiltinNames;
import ast_symtab.Keywords;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeBindings;
import ast_vars.VarDeclarator;
import errors.ErrorLocation;
import tokenize.Ident;

public class ApplyExpression {

  private final SymbolTable symtabApplier;
  private final ClassDeclaration object;
  private final ClassMethodDeclaration method;

  public ApplyExpression(SymbolTable symtabApplier, ClassDeclaration object, ClassMethodDeclaration method) {
    this.symtabApplier = symtabApplier;
    this.object = object;
    this.method = method;
  }

  public SymbolTable getSymtabApplier() {
    return symtabApplier;
  }

  public ClassDeclaration getObject() {
    return object;
  }

  public ClassMethodDeclaration getMethod() {
    return method;
  }

  public void applyExpression(final ExprExpression e) {

    if (e == null) {
      return;
    }
    if (e.getResultType() != null) {
      return;
    }

    if (e.is(ExpressionBase.EUNARY)) {
      applyUnary(e);
    } else if (e.is(ExpressionBase.EBINARY)) {
      applyBinary(e);
    } else if (e.is(ExpressionBase.EASSIGN)) {
      applyAssign(e);
    } else if (e.is(ExpressionBase.EPRIMARY_IDENT)) {
      applyIdentifier(e);
    } else if (e.is(ExpressionBase.EMETHOD_INVOCATION)) {
      applyMethodInvocation(e);
    } else if (e.is(ExpressionBase.EFIELD_ACCESS)) {
      applyFieldAccess(e);
    } else if (e.is(ExpressionBase.ETHIS)) {
      applySelfLiteral(e);
    } else if (e.is(ExpressionBase.EPRIMARY_NUMBER)) {
      applyNumericLiteral(e);
    } else if (e.is(ExpressionBase.ECLASS_CREATION)) {
      applyClassInstanceCreation(e);
    } else if (e.is(ExpressionBase.EPRIMARY_STRING)) {
      applyStringLiteral(e);
    } else if (e.is(ExpressionBase.EPRIMARY_CHAR)) {
      applyPrimaryChar(e);
    } else if (e.is(ExpressionBase.EBOOLEAN_LITERAL)) {
      e.setResultType(TypeBindings.make_boolean());
    } else if (e.is(ExpressionBase.ECAST)) {
      asslyCast(e);
    } else if (e.is(ExpressionBase.ETERNARY_OPERATOR)) {
      appyTernary(e);
    } else if (e.is(ExpressionBase.ESIZEOF)) {
      applySizeof(e);
    } else if (e.is(ExpressionBase.ETYPEOF)) {
      applyTypeof(e);
    } else if (e.is(ExpressionBase.EBUILTIN_FUNC)) {
      applyBuiltinFunc(e);
    } else if (e.is(ExpressionBase.EFOR_LOOP_STEP_COMMA)) {
      applyForLoopComma(e);
    } else if (e.is(ExpressionBase.EDEFAULT_VALUE_FOR_TYPE)) {
      applyDefaultValueFotType(e);
    } else if (e.is(ExpressionBase.ESTATIC_ACCESS)) {
      applyStaticAccess(e);
    } else if (e.is(ExpressionBase.EALLOC)) {
      applyAlloc(e);
    }

    else {
      ErrorLocation.errorExpression("unimpl.expression-type-applier", e);
    }

  }

  private void applyAlloc(ExprExpression e) {
    ExprAlloc node = e.getExprAlloc();
    e.setResultType(new Type(new ClassTypeRef(node.getObject(), node.getObject().getTypeParametersT())));
  }

  private void applyStaticAccess(ExprExpression e) {
    //TODO:STATIC_ACCESS
    ExprStaticAccess node = e.getExprStaticAccess();
    e.setResultType(node.getType());
  }

  private void applyDefaultValueFotType(ExprExpression e) {
    e.setResultType(e.getExprDefaultValueForType().getType());
  }

  private void applyForLoopComma(ExprExpression e) {
    final ExprForLoopStepComma node = e.getExprForLoopStepComma();
    applyExpression(node.getLhs());
    applyExpression(node.getRhs());
    e.setResultType(node.getRhs().getResultType());
  }

  private void applyPrimaryChar(final ExprExpression e) {
    e.setResultType(TypeBindings.make_char());
  }

  private void applyBuiltinFunc(ExprExpression e) {

    final ExprBuiltinFunc node = e.getExprBuiltinFunc();
    final Ident name = node.getName();
    final Type voidType = new Type(e.getBeginPos());

    for (ExprExpression arg : node.getArgs()) {
      applyExpression(arg);
    }

    if (node.getArgs().isEmpty()) {
      ErrorLocation.errorExpression("empty args: " + name.toString(), e);
    }

    final ExprExpression firstArg = node.getArgs().get(0);

    if (name.equals(Keywords.assert_true_ident)) {
      /// void assert_true(int cnd, const char *file, int line, const char *expr)
      if (node.getArgs().size() != 1) {
        ErrorLocation.errorExpression("assert_true expects one argument", e);
      }
      checkIsBoolean(firstArg);
      e.setResultType(voidType);
    }

    else if (name.equals(Keywords.static_assert_ident)) {
      if (node.getArgs().size() != 1) {
        ErrorLocation.errorExpression("static_assert expects one argument", e);
      }
      long res = ConstexprEval.ce(firstArg);
      if (res == 0) {
        ErrorLocation.errorExpression("static_assert fails: ", firstArg);
      }
      e.setResultType(voidType);
    }

    else if (name.equals(Keywords.types_are_same_ident)) {
      if (node.getArgs().size() != 2) {
        ErrorLocation.errorExpression("types_are_equal expects two arguments", e);
      }
      e.setResultType(TypeBindings.make_boolean());
    }

    else if (TypeTraitsUtil.isBuiltinTypeTraitsIdent(name)) {
      if (node.getArgs().size() != 1) {
        ErrorLocation.errorExpression(name.toString() + " expects one argument", e);
      }
      e.setResultType(TypeBindings.make_boolean());
    }

    else if (name.equals(Keywords.is_alive_ident) || name.equals(Keywords.set_deletion_bit_ident)) {
      if (node.getArgs().size() != 1) {
        ErrorLocation.errorExpression(name.toString() + " expects one argument", e);
      }
      e.setResultType(TypeBindings.make_boolean()); // TODO: has=boolean, set=void
    }

    else {
      ErrorLocation.errorExpression("unimpl.builtin function:", e);
    }
  }

  private void applyTypeof(ExprExpression e) {
    ExprTypeof node = e.getExprTypeof();
    applyExpression(node.getExpr());
    e.setResultType(TypeBindings.make_boolean());
  }

  private void applySizeof(ExprExpression e) {
    @SuppressWarnings("unused")
    ExprSizeof exprSizeof = e.getExprSizeof();
    e.setResultType(TypeBindings.make_int()); /// TODO:size
  }

  private void appyTernary(ExprExpression e) {
    ExprTernaryOperator ternaryOperator = e.getTernaryOperator();
    applyExpression(ternaryOperator.getCondition());
    applyExpression(ternaryOperator.getTrueResult());
    applyExpression(ternaryOperator.getFalseResult());

    checkIsBoolean(ternaryOperator.getCondition());

    Type trueType = ternaryOperator.getTrueResult().getResultType();
    Type falseType = ternaryOperator.getFalseResult().getResultType();
    if (!trueType.isEqualTo(falseType)) {
      ErrorLocation.errorExpression("ternary operands types are diferent", e);
    }
    e.setResultType(falseType);
  }

  private void asslyCast(final ExprExpression e) {
    // TODO:
    final ExprCast castExpression = e.getCastExpression();
    final Type toType = castExpression.getToType();
    final ExprExpression expressionForCast = castExpression.getExpressionForCast();

    applyExpression(expressionForCast);

    boolean castIsOk = castExpression.getExpressionForCast().getResultType().isPrimitive() && toType.isPrimitive();
    if (!castIsOk) {
      ErrorLocation.errorExpression("cast expression is suitable only for primitives", e);
    }

    e.setResultType(toType);
  }

  private void applyNumericLiteral(final ExprExpression e) {
    e.setResultType(e.getNumber().getType());
  }

  private void applySelfLiteral(final ExprExpression e) {
    final ClassDeclaration clazz = e.getSelfExpression();
    final ClassTypeRef ref = new ClassTypeRef(clazz, clazz.getTypeParametersT());
    e.setResultType(new Type(ref));
  }

  private void applyStringLiteral(final ExprExpression e) {

    ///// we should find the 'string' class here,
    ///// and we sure that the generic field 'buffer' is
    ///// fully expanded, and we can use it as the result type
    /////
    //final ClassDeclaration stringClass = symtabApplier.getTypename(BuiltinNames.string_ident);
    //final VarDeclarator field = stringClass.getField(Hash_ident.getHashedIdent("buffer"));
    //
    //e.setResultType(field.getType());

    e.setResultType(new Type(new ClassTypeRef(symtabApplier.getTypename(BuiltinNames.str_ident), new ArrayList<>())));
  }

  private void applyClassInstanceCreation(final ExprExpression e) {
    final ExprClassCreation classCreation = e.getClassCreation();
    applyArgs(classCreation.getArguments());

    // type tp = new type(1)

    final Type resultTypeOfObject = classCreation.getType();
    if (resultTypeOfObject == null || resultTypeOfObject.isPrimitive()) {
      ErrorLocation.errorExpression("expect reference for method invocation like [a.b()] -> a must be a class.", e);
    }

    final ClassDeclaration clazz = resultTypeOfObject.getClassTypeFromRef();
    final ClassMethodDeclaration constructor = clazz.getConstructor(classCreation.getArguments());

    if (constructor == null) {
      ErrorLocation.errorExpression("class has no constructor with args: "
          + ArgsListToString.paramsToStringWithBraces(classCreation.getArguments(), '('), e);
    }

    e.setResultType(resultTypeOfObject);
    classCreation.setConstructor(constructor);
  }

  private void applyAssign(final ExprExpression e) {
    final ExprAssign node = e.getAssign();

    final ExprExpression lvalue = node.getLvalue();
    final ExprExpression rvalue = node.getRvalue();

    applyExpression(lvalue);
    applyExpression(rvalue);

    LvalueUtil.checkHard(lvalue, e, method);

    final Type lhsType = node.getLvalue().getResultType();
    final Type rhsType = node.getRvalue().getResultType();
    if (!lhsType.isEqualTo(rhsType)) {
      ErrorLocation.errorExpression("types are different for assign", e);
    }

    e.setResultType(lhsType);
  }

  private void applyUnary(final ExprExpression e) {
    final ExprUnary node = e.getUnary();
    applyExpression(node.getOperand());
    ApplyExpressionType.setUnaryType(e);
  }

  private void applyBinary(final ExprExpression e) {
    final ExprBinary node = e.getBinary();
    applyExpression(node.getLhs());
    applyExpression(node.getRhs());
    ApplyExpressionType.setBinaryType(e);
  }

  private void applyIdentifier(final ExprExpression e) {

    /// the only one thing that we should to do here:
    /// to find what the 'id' is, and bind the
    /// found symbol to the ExprIdent, and set the result
    /// for this expression as a type of this symbol
    /// we had found.
    /// do not apply any initializers, etc, with this variable here,
    /// because it is not a goal...

    final ExprIdent primaryIdent = e.getIdent();

    final Symbol sym = symtabApplier.findVar(primaryIdent.getIdentifier(), F_ALL);
    if (sym == null) {
      ErrorLocation.errorExpression("symbol was not declared in this scope", e);
    }
    if (sym.isVariable()) {
      e.setResultType(sym.getType());
      primaryIdent.setVar(sym.getVariable());
    }

    else {
      final ClassDeclaration clazz = sym.getClazz();
      final Type resultType = new Type(new ClassTypeRef(clazz, clazz.getTypeParametersT()));

      if (clazz.isEnum()) {
        e.setResultType(resultType);
        primaryIdent.setStaticEnumAccess(clazz);
      }

      else {
        e.setResultType(resultType);
        primaryIdent.setStaticClass(clazz);
      }
    }

  }

  private ClassDeclaration getClassFromObject(final ExprExpression obj, final ExprExpression orig) {
    final Type resultTypeOfObject = obj.getResultType(); // must be a reference!
    if (resultTypeOfObject == null) {
      ErrorLocation.errorExpression("type unspecified for field-access expression:", orig);
    }
    boolean isOkAccess = resultTypeOfObject.isClass();
    if (!isOkAccess) {
      ErrorLocation.errorExpression("member-access-expression expects a class", orig);
    }
    return resultTypeOfObject.getClassTypeFromRef();
  }

  private void applyFieldAccess(final ExprExpression e) {

    final ExprFieldAccess fieldAccess = e.getFieldAccess();
    applyExpression(fieldAccess.getObject());

    final Ident fieldName = fieldAccess.getFieldName();
    final String fieldNameToString = fieldName.getName();

    // find the field, and get its type

    final ClassDeclaration clazz = getClassFromObject(fieldAccess.getObject(), e);
    final VarDeclarator field = clazz.getField(fieldName);

    if (field == null) {
      ErrorLocation.errorExpression("class has no field: " + fieldNameToString, e);
    }

    e.setResultType(field.getType());
    fieldAccess.setField(field);

  }

  private void applyMethodInvocation(final ExprExpression e) {

    final ExprMethodInvocation methodInvocation = e.getMethodInvocation();
    applyExpression(methodInvocation.getObject());
    applyArgs(methodInvocation.getArguments());

    // a.fn(1,2,3)
    // self.fn(1,2,3)

    final ClassDeclaration clazz = getClassFromObject(methodInvocation.getObject(), e);
    ClassMethodDeclaration method = null;
    if (methodInvocation.getFuncname().equals(Keywords.deinit_ident)) {
      method = clazz.getDestructor();
    } else {
      method = clazz.getMethod(methodInvocation.getFuncname(), methodInvocation.getArguments());
    }

    if (method == null) {
      ErrorLocation.errorExpression("class has no method: " + methodInvocation.getFuncname().getName(), e);
    }

    e.setResultType(method.getType());
    methodInvocation.setMethod(method);

  }

  private void applyArgs(final List<ExprExpression> arguments) {
    for (ExprExpression arg : arguments) {
      applyExpression(arg);
    }
  }

  private void checkIsBoolean(ExprExpression e) {
    // that's ok - the test expression of the for-loop may be null
    // for example.
    if (e == null) {
      return;
    }
    if (!e.getResultType().isBoolean()) {
      ErrorLocation.errorExpression("expected boolean type: ", e);
    }
  }

}
