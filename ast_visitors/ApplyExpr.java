package njast.ast_visitors;

import java.util.List;

import jscan.tokenize.Token;
import njast.ast_kinds.ExpressionBase;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.methods.ClassMethodDeclaration;
import njast.ast_nodes.clazz.vars.VarDeclarator;
import njast.ast_nodes.expr.ExprBinary;
import njast.ast_nodes.expr.ExprExpression;
import njast.ast_nodes.expr.ExprFieldAccess;
import njast.ast_nodes.expr.ExprMethodInvocation;
import njast.ast_nodes.expr.ExprPrimaryIdent;
import njast.errors.EParseException;
import njast.types.ReferenceType;
import njast.types.Type;

public class ApplyExpr {

  private final ApplyCompilationUnit typeApplier;

  public ApplyExpr(ApplyCompilationUnit typeApplier) {
    this.typeApplier = typeApplier;
  }

  public boolean applyExpr(ClassDeclaration object, ExprExpression e) {

    if (e == null) {
      return false;
    }
    if (e.getResultType() != null) {
      return false;
    }

    ExpressionBase base = e.getBase();

    if (base == ExpressionBase.EBINARY) {
      applyBinary(object, e);
    }

    else if (base == ExpressionBase.EPRIMARY_IDENT) {
      applyIdentifier(e);
    }

    else if (base == ExpressionBase.EMETHOD_INVOCATION) {
      applyMethodInvocation(object, e);
    }

    else if (base == ExpressionBase.EFIELD_ACCESS) {
      applyFieldAccess(object, e);
    }

    else if (base == ExpressionBase.ETHIS) {
      e.setResultType(new Type(new ReferenceType(object)));
    }

    else if (base == ExpressionBase.EPRIMARY_NUMBER) {
      e.setResultType(Type.INT_TYPE); // TODO:
    }

    else {
      throw new EParseException("unimpl. expr.:" + base.toString());
    }

    return true;

  }

  private void applyBinary(ClassDeclaration object, ExprExpression e) {
    ExprBinary node = e.getBinary();
    Token operator = node.getOperator();

    final ExprExpression LHS = node.getLhs();
    final ExprExpression RHS = node.getRhs();

    applyExpr(object, LHS);
    applyExpr(object, RHS);

    e.setResultType(node.getLhs().getResultType());
  }

  private void applyIdentifier(ExprExpression e) {
    ExprPrimaryIdent primaryIdent = e.getLiteralIdentifier();

    Symbol sym = typeApplier.findBindingFromIdentifierToTypename(primaryIdent.getIdentifier());

    if (sym.isClassType()) {
      throw new EParseException("unimpl.");
    }

    // set type to expression
    final VarDeclarator variable = sym.getVariable();
    e.setResultType(variable.getType());

    // remember var
    primaryIdent.setVariable(variable);
  }

  private void applyFieldAccess(ClassDeclaration object, ExprExpression e) {

    ExprFieldAccess fieldAccess = e.getFieldAccess();
    applyExpr(object, fieldAccess.getObject());

    // find the field, and get its type
    //
    final Type resultTypeOfObject = fieldAccess.getObject().getResultType(); // must be a reference!
    if (resultTypeOfObject == null || resultTypeOfObject.isPrimitive()) {
      throw new EParseException("expect reference for field access like [a.b] -> a must be a class.");
    }

    final ClassDeclaration whereWeWantToFindTheField = resultTypeOfObject.getReferenceType().getTypeName();
    final VarDeclarator field = whereWeWantToFindTheField.getField(fieldAccess.getFieldName());

    if (field == null) {
      throw new EParseException("class has no field: " + fieldAccess.getFieldName().getName());
    }

    e.setResultType(field.getType());

  }

  private void applyMethodInvocation(ClassDeclaration object, ExprExpression e) {

    ExprMethodInvocation methodInvocation = e.getMethodInvocation();
    applyExpr(object, methodInvocation.getObject());

    final List<ExprExpression> arguments = methodInvocation.getArguments();
    for (ExprExpression arg : arguments) {
      applyExpr(object, arg);
    }

    if (methodInvocation.isMethodInvocation()) {
      // method: a.fn(1,2,3)

      final Type resultTypeOfObject = methodInvocation.getObject().getResultType(); // must be a reference!
      if (resultTypeOfObject == null || resultTypeOfObject.isPrimitive()) {
        throw new EParseException("expect reference for method invocation like [a.b()] -> a must be a class.");
      }

      final ClassDeclaration whereWeWantToFindTheMethod = resultTypeOfObject.getReferenceType().getTypeName();
      final ClassMethodDeclaration method = whereWeWantToFindTheMethod.getMethod(methodInvocation.getFuncname(),
          arguments);

      if (method == null) {
        throw new EParseException("class has no method: " + methodInvocation.getFuncname().getName());
      }

      e.setResultType(method.getResultType());

    } else {
      // function: fn(1,2,3)
      ClassMethodDeclaration func = object.getMethod(methodInvocation.getFuncname(), arguments);
      if (func == null) {
        throw new EParseException("class has no method: " + methodInvocation.getFuncname().getName());
      }
      e.setResultType(func.getResultType());
    }

  }

}
