package njast.ast.nodes.expr;

import java.io.Serializable;

import jscan.cstrtox.C_strtox;
import jscan.tokenize.Token;
import njast.ast.kinds.ExpressionBase;
import njast.types.Type;

public class ExprExpression implements Serializable {
  private static final long serialVersionUID = -2905880039842730533L;

  // main
  private final ExpressionBase base; // what union contains
  private Type resultType;

  // nodes
  private ExprUnary unary;
  private ExprBinary binary;
  private ExprNumber number;
  private ExprIdent ident;
  private ExprMethodInvocation methodInvocation;
  private ExprFieldAccess fieldAccess;
  private ExprClassCreation classCreation;
  private ExprAssign assign;
  private ExprArrayCreation arrayCreation;
  private ExprSelf selfExpression;

  public ExprExpression(ExprSelf selfExpression) {
    this.base = ExpressionBase.ESELF;
    this.selfExpression = selfExpression;
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

  public ExprExpression(C_strtox e, Token token) {
    e.ev(); // XXX:

    this.base = ExpressionBase.EPRIMARY_NUMBER;

    ExprNumber number = null;
    if (e.isIntegerKind()) {
      number = new ExprNumber(e.getClong(), e.getNumtype());
    } else {
      number = new ExprNumber(e.getCdouble(), e.getNumtype());
    }

    this.number = number;
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

  public ExprNumber getNumber() {
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
      return String.format("%d", number.getClong());
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
    return base.toString();
  }

}