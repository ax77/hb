package njast.ast_visitors;

import jscan.tokenize.Token;
import njast.ast_kinds.ExpressionBase;
import njast.ast_nodes.clazz.ClassDeclaration;
import njast.ast_nodes.clazz.ClassFieldDeclaration;
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
      ExprBinary node = e.getBinary();
      Token operator = node.getOperator();

      final ExprExpression LHS = node.getLhs();
      final ExprExpression RHS = node.getRhs();

      applyExpr(object, LHS);
      applyExpr(object, RHS);

      e.setResultType(node.getLhs().getResultType());

    }

    else if (base == ExpressionBase.EPRIMARY_IDENT) {
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

    else if (base == ExpressionBase.EMETHOD_INVOCATION) {
      ExprMethodInvocation methodInvocation = e.getMethodInvocation();
      applyExpr(object, methodInvocation.getObject());

      //TODO:here
    }

    else if (base == ExpressionBase.EFIELD_ACCESS) {
      ExprFieldAccess fieldAccess = e.getFieldAccess();
      applyExpr(object, fieldAccess.getObject());

      // find the field, and get its type
      //
      final Type resultTypeOfObject = fieldAccess.getObject().getResultType(); // must be a reference!
      if (resultTypeOfObject == null || resultTypeOfObject.isPrimitive()) {
        throw new EParseException("expect reference for field access like [a.b] -> a must be a class.");
      }

      final ClassDeclaration whereWeWantToFindTheField = resultTypeOfObject.getReferenceType().getTypeName();
      final ClassFieldDeclaration field = whereWeWantToFindTheField.getField(fieldAccess.getFieldName());

      if (field == null) {
        throw new EParseException("class has no field: " + fieldAccess.getFieldName().getName());
      }

      e.setResultType(field.getField().getType());

    }

    else if (base == ExpressionBase.ETHIS) {
      e.setResultType(new Type(new ReferenceType(object)));
    }

    else {
      throw new EParseException("unimpl. expr.:" + base.toString());
    }

    return true;

  }

}
