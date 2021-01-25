package ast_expr;

import java.io.Serializable;

import ast_types.Type;
import literals.IntLiteral;
import tokenize.Token;

public class ExprExpression implements Serializable {
  private static final long serialVersionUID = -2905880039842730533L;

  // main
  private final ExpressionBase base; // what union contains
  private Type resultType;

  // nodes
  private ExprUnary unary;
  private ExprBinary binary;
  private IntLiteral number;
  private ExprIdent ident;
  private ExprMethodInvocation methodInvocation;
  private ExprFieldAccess fieldAccess;
  private ExprClassCreation classCreation;
  private ExprAssign assign;
  private ExprArrayCreation arrayCreation;
  private ExprSelf selfExpression;
  private String stringConst;
  private ExprArrayAccess arrayAccess;

  public ExprExpression(ExprArrayAccess arrayAccess) {
    this.base = ExpressionBase.EARRAY_ACCESS;
    this.arrayAccess = arrayAccess;
  }

  public ExprArrayAccess getArrayAccess() {
    return arrayAccess;
  }

  public ExprExpression(ExprSelf selfExpression) {
    this.base = ExpressionBase.ESELF;
    this.selfExpression = selfExpression;
  }

  public ExprExpression(String stringConst) {
    this.base = ExpressionBase.ESTRING_CONST;
    this.stringConst = stringConst;
  }

  public String getStringConst() {
    return stringConst;
  }

  public ExprExpression(ExprAssign assign) {
    this.base = ExpressionBase.EASSIGN;
    this.assign = assign;
  }

  public ExprExpression(ExprArrayCreation arrayCreation) {
    this.base = ExpressionBase.EARRAY_INSTANCE_CREATION;
    this.arrayCreation = arrayCreation;
  }

  public ExprExpression(ExpressionBase base) {
    this.base = base;
  }

  public ExprExpression(ExprClassCreation classCreation) {
    this.base = ExpressionBase.ECLASS_INSTANCE_CREATION;
    this.classCreation = classCreation;
  }

  public ExprExpression(ExprUnary unary) {
    this.base = ExpressionBase.EUNARY;
    this.unary = unary;
  }

  public ExprExpression(ExprBinary binary) {
    this.base = ExpressionBase.EBINARY;
    this.binary = binary;
  }

  public ExprExpression(IntLiteral e, Token token) {
    this.base = ExpressionBase.EPRIMARY_NUMBER;
    this.number = e;
  }

  public ExprExpression(ExprMethodInvocation methodInvocation) {
    this.base = ExpressionBase.EMETHOD_INVOCATION;
    this.methodInvocation = methodInvocation;
  }

  public ExprExpression(ExprFieldAccess fieldAccess) {
    this.base = ExpressionBase.EFIELD_ACCESS;
    this.fieldAccess = fieldAccess;
  }

  public ExprExpression(ExprIdent symbol) {
    this.base = ExpressionBase.EPRIMARY_IDENT;
    this.ident = symbol;
  }

  public ExprArrayCreation getArrayInstanceCreation() {
    return arrayCreation;
  }

  public ExprAssign getAssign() {
    return assign;
  }

  public Type getResultType() {
    return resultType;
  }

  public ExprSelf getSelfExpression() {
    return selfExpression;
  }

  public void setResultType(Type resultType) {
    this.resultType = resultType;
  }

  public ExpressionBase getBase() {
    return base;
  }

  public ExprUnary getUnary() {
    return unary;
  }

  public ExprBinary getBinary() {
    return binary;
  }

  public ExprIdent getIdent() {
    return ident;
  }

  public IntLiteral getNumber() {
    return number;
  }

  public ExprMethodInvocation getMethodInvocation() {
    return methodInvocation;
  }

  public ExprFieldAccess getFieldAccess() {
    return fieldAccess;
  }

  public ExprClassCreation getClassCreation() {
    return classCreation;
  }

  public ExprArrayCreation getArrayCreation() {
    return arrayCreation;
  }

  public boolean is(ExpressionBase what) {
    return base.equals(what);
  }

  @Override
  public String toString() {
    if (base == ExpressionBase.EBINARY) {
      return binary.toString();
    }
    if (base == ExpressionBase.EASSIGN) {
      return assign.toString();
    }
    if (base == ExpressionBase.EFIELD_ACCESS) {
      return fieldAccess.toString();
    }
    if (base == ExpressionBase.EPRIMARY_IDENT) {
      return ident.toString();
    }
    if (base == ExpressionBase.ESELF) {
      return selfExpression.toString();
    }
    if (base == ExpressionBase.EMETHOD_INVOCATION) {
      return methodInvocation.toString();
    }
    if (base == ExpressionBase.EPRIMARY_NUMBER) {
      return number.toString();
    }
    if (base == ExpressionBase.EPRIMARY_NULL_LITERAL) {
      return "null";
    }
    if (base == ExpressionBase.ECLASS_INSTANCE_CREATION) {
      return classCreation.toString();
    }
    if (base == ExpressionBase.EARRAY_INSTANCE_CREATION) {
      return arrayCreation.toString();
    }
    if (base == ExpressionBase.ESTRING_CONST) {
      return stringConst;
    }
    if (base == ExpressionBase.EARRAY_ACCESS) {
      return arrayAccess.toString();
    }
    return base.toString();
  }

}
