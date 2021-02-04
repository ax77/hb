package ast_st2_annotate;

import static ast_st2_annotate.SymbolTable.F_ALL;

import java.util.ArrayList;
import java.util.List;

import ast_class.ClassDeclaration;
import ast_expr.ExprArrayAccess;
import ast_expr.ExprArrayCreation;
import ast_expr.ExprArrayProperty;
import ast_expr.ExprAssign;
import ast_expr.ExprBinary;
import ast_expr.ExprClassCreation;
import ast_expr.ExprExpression;
import ast_expr.ExprFieldAccess;
import ast_expr.ExprIdent;
import ast_expr.ExprMethodInvocation;
import ast_expr.ExprSelf;
import ast_expr.ExprUnary;
import ast_expr.ExpressionBase;
import ast_expr.FuncArg;
import ast_method.ClassMethodDeclaration;
import ast_types.ArrayType;
import ast_types.ClassTypeRef;
import ast_types.Type;
import ast_types.TypeBindings;
import ast_vars.VarBase;
import ast_vars.VarDeclarator;
import errors.AstParseException;
import errors.ErrorLocation;
import hashed.Hash_ident;
import tokenize.Ident;

public class SymExpressionApplier {

  private final SymbolTable symtabApplier;

  public SymExpressionApplier(SymbolTable symtabApplier) {
    this.symtabApplier = symtabApplier;
  }

  public void applyExpression(final ClassDeclaration object, final ExprExpression e) {

    if (e == null) {
      return;
    }
    if (e.getResultType() != null) {
      // we may rewrite some expressions, so: TODO:
      // return;
    }

    if (e.is(ExpressionBase.EUNARY)) {
      applyUnary(object, e);
    }

    else if (e.is(ExpressionBase.EBINARY)) {
      applyBinary(object, e);
    }

    else if (e.is(ExpressionBase.EASSIGN)) {
      applyAssign(object, e);
    }

    else if (e.is(ExpressionBase.EPRIMARY_IDENT)) {
      applyIdentifier(object, e);

      // if it is possible to replace each 'id' with 'self.id'
      // if 'id' is a class-field
      // if 'id' a local variable - we'll just find the symbol, and bind it
      final VarDeclarator variable = e.getIdent().getVariable();
      if (variable == null) {
        ErrorLocation.errorExpression("symbol not found", e);
      }
      if (maybeReplaceIdentWithFieldAccess(variable, object, e)) {
        applyExpression(object, e);
      }

    }

    else if (e.is(ExpressionBase.EMETHOD_INVOCATION)) {
      applyMethodInvocation(object, e);
    }

    else if (e.is(ExpressionBase.EFIELD_ACCESS)) {
      applyFieldAccess(object, e);
    }

    else if (e.is(ExpressionBase.ESELF)) {
      applySelfLiteral(e);
    }

    else if (e.is(ExpressionBase.EPRIMARY_NUMBER)) {
      applyNumericLiteral(e);
    }

    else if (e.is(ExpressionBase.EPRIMARY_NULL_LITERAL)) {
      // TODO:
    }

    else if (e.is(ExpressionBase.ECLASS_INSTANCE_CREATION)) {
      applyClassInstanceCreation(object, e);
    }

    else if (e.is(ExpressionBase.ESTRING_CONST)) {
      applyStringLiteral(e);
    }

    else if (e.is(ExpressionBase.EARRAY_ACCESS)) {
      applyArrayAccess(object, e);
    }

    else if (e.is(ExpressionBase.ECHAR_CONST)) {
      e.setResultType(TypeBindings.make_u8(e.getBeginPos()));
    }

    else if (e.is(ExpressionBase.EBOOLEAN_LITERAL)) {
      e.setResultType(TypeBindings.make_boolean(e.getBeginPos())); // TODO: ?
    }

    else if (e.is(ExpressionBase.EARRAY_INSTANCE_CREATION)) {
      applyArrayCreation(e);
    }

    else {
      ErrorLocation.errorExpression("unimpl.expression-type-applier", e);
    }

  }

  private void checkArraySize(final ArrayType array, final ExprExpression e) {
    if (array.getLength() == 0) {
      ErrorLocation.errorExpression("array-creation with zero-size", e);
    }
    if (array.getType().is_array()) {
      checkArraySize(array.getType().getArrayType(), e);
    }
  }

  private void applyArrayCreation(final ExprExpression e) {
    final ExprArrayCreation arrayCreation = e.getArrayCreation();
    final Type type = arrayCreation.getType();
    final ArrayType array = type.getArrayType();
    checkArraySize(array, e);
    e.setResultType(type);
  }

  private void applyNumericLiteral(final ExprExpression e) {
    e.setResultType(e.getNumber().getType());
  }

  private void applySelfLiteral(final ExprExpression e) {
    final ClassDeclaration clazz = e.getSelfExpression().getClazz();
    final ArrayList<Type> typeArguments = new ArrayList<>();
    final ClassTypeRef ref = new ClassTypeRef(clazz, typeArguments);
    e.setResultType(new Type(ref, e.getBeginPos()));
  }

  private void applyStringLiteral(final ExprExpression e) {
    String strconst = e.getBeginPos().getValue(); // TODO:__string__
    ArrayType arrtype = new ArrayType(TypeBindings.make_u8(e.getBeginPos()), strconst.length());
    e.setResultType(new Type(arrtype, e.getBeginPos()));
  }

  private void applyArrayAccess(final ClassDeclaration object, final ExprExpression e) {
    ExprArrayAccess arrayAccess = e.getArrayAccess();
    applyExpression(object, arrayAccess.getArray());
    applyExpression(object, arrayAccess.getIndex());
    Type arrtype = arrayAccess.getArray().getResultType();
    if (!arrtype.is_array()) {
      ErrorLocation.errorExpression("expect array", e);
    }
    Type arrof = arrtype.getArrayType().getType();
    e.setResultType(arrof);
  }

  private void applyClassInstanceCreation(final ClassDeclaration object, final ExprExpression e) {
    ExprClassCreation classInstanceCreation = e.getClassCreation();

    for (FuncArg arg : classInstanceCreation.getArguments()) {
      applyExpression(object, arg.getExpression());
    }

    e.setResultType(classInstanceCreation.getType());
  }

  private void applyAssign(final ClassDeclaration object, final ExprExpression e) {
    ExprAssign node = e.getAssign();

    final ExprExpression lvalue = node.getLvalue();
    Lvalue.checkHard(lvalue);

    final ExprExpression rvalue = node.getRvalue();

    applyExpression(object, lvalue);
    applyExpression(object, rvalue);

    final Type lhsType = node.getLvalue().getResultType();
    if (rvalue.is(ExpressionBase.EPRIMARY_NULL_LITERAL)) {
      // TODO:
    }

    else {
      final Type rhsType = node.getRvalue().getResultType();
      if (!lhsType.is_equal_to(rhsType)) {
        ErrorLocation.errorExpression("types are different for assign", e);
      }
    }

    e.setResultType(lhsType);
  }

  private void applyUnary(final ClassDeclaration object, final ExprExpression e) {
    final ExprUnary node = e.getUnary();
    applyExpression(object, node.getOperand());
    SymExpressionTypeResolver.setUnaryType(e);
  }

  private void applyBinary(final ClassDeclaration object, final ExprExpression e) {
    final ExprBinary node = e.getBinary();
    applyExpression(object, node.getLhs());
    applyExpression(object, node.getRhs());
    SymExpressionTypeResolver.setBinaryType(e);
  }

  private void applyIdentifier(final ClassDeclaration object, final ExprExpression e) {

    // the only one thing that we should to do here:
    // to find what the 'id' is, and bind the
    // found symbol to the ExprIdent, and set the result
    // for this expression as a type of this symbol
    // we had found.
    // do not apply any initializers, etc, with this variable here,
    // because it is not a goal...

    final ExprIdent primaryIdent = e.getIdent();

    final Symbol sym = symtabApplier.findVar(primaryIdent.getIdentifier(), F_ALL);
    if (sym == null) {
      ErrorLocation.errorExpression("symbol was not declared in this scope", e);
    }

    if (sym.isClassType()) {
      throw new AstParseException("unimpl.");
    }

    // set type to expression
    final VarDeclarator variable = sym.getVariable();
    e.setResultType(variable.getType());

    //MIR:TREE
    primaryIdent.setVariable(variable);
  }

  private boolean maybeReplaceIdentWithFieldAccess(final VarDeclarator variable, final ClassDeclaration object,
      final ExprExpression e) {
    if (variable.getBase() == VarBase.CLASS_FIELD) {
      final Ident fieldName = variable.getIdentifier();
      final ExprExpression selfExpression = new ExprExpression(new ExprSelf(object), variable.getBeginPos());
      final ExprFieldAccess fieldAccess = new ExprFieldAccess(selfExpression, fieldName);
      fieldAccess.setField(variable);
      e.replaceIdentWithFieldAccess(fieldAccess);
      return true;
    }
    return false;
  }

  private void applyFieldAccess(final ClassDeclaration object, final ExprExpression e) {

    final ExprFieldAccess fieldAccess = e.getFieldAccess();
    applyExpression(object, fieldAccess.getObject());

    final Ident fieldName = fieldAccess.getFieldName();
    final String fieldNameToString = fieldName.getName();

    // find the field, and get its type

    final Type resultTypeOfObject = fieldAccess.getObject().getResultType(); // must be a reference!
    if (resultTypeOfObject == null) {
      ErrorLocation.errorExpression("type unspecified for field-access expression:", e);
    }
    boolean isOkAccess = resultTypeOfObject.is_class() || resultTypeOfObject.is_array();
    if (!isOkAccess) {
      ErrorLocation
          .errorExpression("expect reference for field access like [a.b] -> a must be a class, or array.length", e);
    }

    // array-property access

    if (resultTypeOfObject.is_array()) {
      final Ident expectedProperty = Hash_ident.getHashedIdent("length");
      if (!fieldName.equals(expectedProperty)) {
        ErrorLocation.errorExpression("array has no property: " + fieldNameToString, e);
      }

      final ExprArrayProperty arrayProperty = new ExprArrayProperty(fieldAccess.getObject(), expectedProperty);
      e.replaceFieldAccessWithArrayProperty(arrayProperty);
      e.setResultType(TypeBindings.make_u64(e.getBeginPos()));
    }

    // class-field access

    else {
      final ClassDeclaration whereWeWantToFindTheField = resultTypeOfObject.getClassTypeFromRef();
      final VarDeclarator field = whereWeWantToFindTheField.getField(fieldName);

      if (field == null) {
        ErrorLocation.errorExpression("class has no field: " + fieldNameToString, e);
      }

      e.setResultType(field.getType());

      //MIR:TREE
      fieldAccess.setField(field);
    }

  }

  private void applyMethodInvocation(final ClassDeclaration object, final ExprExpression e) {

    final ExprMethodInvocation methodInvocation = e.getMethodInvocation();
    applyExpression(object, methodInvocation.getObject());
    applyMethodCallArgs(object, methodInvocation);

    // a.fn(1,2,3)
    // self.fn(1,2,3)

    final Type resultTypeOfObject = methodInvocation.getObject().getResultType(); // must be a reference!
    if (resultTypeOfObject == null || resultTypeOfObject.is_primitive()) {
      ErrorLocation.errorExpression("expect reference for method invocation like [a.b()] -> a must be a class.", e);
    }

    final ClassDeclaration whereWeWantToFindTheMethod = resultTypeOfObject.getClassTypeFromRef();
    final ClassMethodDeclaration method = whereWeWantToFindTheMethod.getMethod(methodInvocation.getFuncname(),
        methodInvocation.getArguments());

    if (method == null) {
      ErrorLocation.errorExpression("class has no method: " + methodInvocation.getFuncname().getName(), e);
    }

    e.setResultType(method.getType());

    //MIR:TREE
    methodInvocation.setMethod(method);

  }

  private void applyMethodCallArgs(final ClassDeclaration object, final ExprMethodInvocation methodInvocation) {
    final List<FuncArg> arguments = methodInvocation.getArguments();
    for (FuncArg arg : arguments) {
      applyExpression(object, arg.getExpression());
    }
  }

}
